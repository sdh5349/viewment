<template>
  <div v-if="articleInfo">
    <v-card>
    <v-container>
      <v-row>
        <v-col
          cols="10"
          >
          <v-icon >
            {{ icons.mdiAccount }}
          </v-icon>  
          {{ articleInfo.user.nickname }}
        </v-col>
            
        <v-spacer></v-spacer>
      
        <v-col
          cols="2"
        >
          <v-btn icon>
            <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
          </v-btn>

          <v-navigation-drawer
            v-model="drawer"
            absolute
            bottom
            temporary
          >
            <v-list
              nav
              dense
            >
              <v-list-item-group
                v-model="group"
                active-class="deep-purple--text text--accent-4"
              >
                <v-list-item>
                  <v-list-item-title @click='updateArticle'>수정</v-list-item-title>
                </v-list-item>

                <v-list-item>
                  <v-list-item-title @click='deleteArticle'>삭제</v-list-item-title>
                </v-list-item>

              </v-list-item-group>
            </v-list>
          </v-navigation-drawer>

    

        </v-col>
      </v-row>
      <v-row>
        <v-col
          cols='3'
        >
          <v-icon color="green darken-2">mdi-call-split</v-icon>
        </v-col>

        <v-col 
          cols='9'
        >
          {{ articleInfo.pin.address }}
        </v-col>
           
      </v-row>
    </v-container>
  


  <v-card
    elevation="24"
    max-width="100%"
    class="mx-auto"
  >
    <v-system-bar lights-out></v-system-bar>
    <v-carousel
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="300"
      mouse-drag = true
    >
      <v-carousel-item
        v-for="(image, i) in articleInfo.images "
        :key="i"
        :src="SERVER + '/images/'+ image.path">    
      >
      </v-carousel-item>
    </v-carousel>
     
           
  
    <v-card-text>
      <v-row>
        <v-col
          
          v-for="(hashtag, i) in articleInfo.hashtags"
          :key="i"
          
        >  
          <v-btn 
            class="mx-0, px-0"         
            rounded
            @click='clickHashtag(hashtag.contents)'
          >
          #{{ hashtag.contents }}
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>

    <v-card-text>
      {{articleInfo.contents}}
    </v-card-text>

    좋아요 수 스크랩 수
    <v-divider class="mx-4"></v-divider>
    
    
    <v-divider class="mx-4"></v-divider>
    <v-row>
      <v-col>
        <v-btn>
          <v-icon>
            {{ icons.mdiHeart }}
          </v-icon>
        </v-btn>
      </v-col>
      <v-col >
        <v-text-field
            
        ></v-text-field>
      </v-col>

      <v-col >
        <v-btn>
          댓글달기
        </v-btn>
      </v-col>
    </v-row>


  </v-card>
  </v-card>

  </div>
</template>

<script>
  import {
    mdiAccount,
    mdiHeart,
  } from '@mdi/js'
  import axios from 'axios'
  import UpdateArticleVue from './UpdateArticle.vue'
  const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return{
      SERVER: process.env.VUE_APP_SERVER_URL,
      icons: {
        mdiAccount,
        mdiHeart,
      },
      drawer: false,
      group: null,
      // articleId: this.$route.params.articleId,
      articleId: '',
      articleInfo: '',
    }
  },
  methods: {
    getArticle(){

      this.articleId = this.$route.params.articleId
      
      axios.get(`${SERVER_URL}/articles/`+ this.articleId, this.getToken)
      .then((res) => {
        
        this.articleInfo = res.data
        console.log(this.articleInfo)
        
      })
    },
    updateArticle(){
      console.log('update')
      this.$router.push({name: 'UpdateArticle', params: {
        articleId: this.articleId,
        hashtagArray: this.articleInfo.hashtags,
        contents: this.articleInfo.contents,
        lat: this.articleInfo.pin.lat,
        lng: this.articleInfo.pin.lng,
        preview: this.articleInfo.images
      }})
    },
    deleteArticle(){
      axios.delete(`${SERVER_URL}/articles/`+ this.articleId, this.getToken )
      .then((res) => {
        alert('게시물 삭제 완료')
        this.$router.push({name: 'Feed'})
      })
    },
    clickHashtag(res){
      this.$router.push({name: 'Search', params: {
        clickedHash: res
      }})
    }
  },
  watch: {
    group () {
      this.drawer = false
    },
  },
  created() {
    this.getArticle()
  },
  computed: {
    getToken(){
      const token = sessionStorage.getItem('jwt')
      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
}
</script>

<style>

</style>