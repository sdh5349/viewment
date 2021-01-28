<template>
  <div>
    <v-card>
    <v-container>
      <v-row>
        <v-col
          cols="10"
          >
          <v-icon >
            {{ icons.mdiAccount }}
          </v-icon>
          
          {{ articleInfo.article.user.nickname }}
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
          {{ articleInfo.article.pin.address }}
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
    >
      <v-carousel-item
        v-for="(image, i) in articleImage"
        :key="i"
        :src="image.src"
      >
    
      </v-carousel-item>
    </v-carousel>
    
  
    <v-card-text>
      <v-row>
        <v-col
          
          v-for="(hashtag, i) in articleInfo.article.hashtags"
          :key="i"
          
        >  
          <v-btn 
            class="mx-0, px-0"         
            rounded
            
          >
          {{ hashtag.contents }}
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>

    <v-card-text>
      {{articleInfo.article.contents}}
    </v-card-text>

    좋아요 수 스크랩 수
    <v-divider class="mx-4"></v-divider>
    
    <v-card-text
      v-for="(reply, i ) in articleInfo.replies"
      :key=i>
      <v-icon >
        {{ icons.mdiAccount }}
      </v-icon>
      {{reply.user.nickname}}
      {{reply.contents}}
      <v-btn>대 댓글</v-btn> 
      <div
        v-for="(rereply, i) in reply.rereplies"
        :key=i
        class="ml-3"
      >
        <v-icon >
          {{ icons.mdiAccount }}
        </v-icon>
        {{rereply.user.nickname}}
        {{rereply.contents}}
      </div>

    </v-card-text>
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
  const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return{
      icons: {
        mdiAccount,
        mdiHeart,
      },
      drawer: false,
      group: null,
      articleInfo: {
        "article": {
          "articleId": 0,
          "contents": "겨울에 눈이와서 흰색으로 바뀐 유성연수원입니다.",
          "wdate": "string",
          "user": {
            "userId": "string",
            "nickname": "shindonghyun",
            "profileImage": {
              "imageId": 0,
              "path":"string",
            },
              },
              "pin": {
                "pinId": "string",
                "address": "삼성화재유성연수원",
                "lat": 0.0,
                "lng": 0.0
              },
                "images": [
                  {
                    "imageId": 0,
                    "path": "string"
                  }
                ],
                  "hashtags": [
                    {
                      "hashtagId": 0,
                      "contents": "#유성연수원"
                    },
                    {
                      "hashtagId": 1,
                      "contents": "#SSAFY"
                    },
                    {
                      "hashtagId": 2,
                      "contents": "#겨울"
                    },
                  ]
     },
     "replies": [
        {
          "replyId": 0,
          "articleId": 0,
          "contents": "연수원 가고싶다..",
          "wdate": "string",
          "user": {
            "uerId": "string",
            "nickname": "싸피4기-1",
            "profileImage": {
              "imageId": 0,
              "path" : "string"
            }
          },
            "rereplies": [
              {
                "replyId": 0,
                "articleId": 0,
                "contents": "나도...",
                "wdate": "string",
                "user": {
                  "userId": "string",
                    "nickname": "싸피 4기-2",
                    "profileImage": {
                      "imageId": 0,
                      "path": "string"
                  }
                }
              },
                {
                "replyId": 0,
                "articleId": 0,
                "contents": "싸피 밥 먹고싶다",
                "wdate": "string",
                "user": {
                  "userId": "string",
                    "nickname": "싸피 4기-3",
                    "profileImage": {
                      "imageId": 0,
                      "path": "string"
                  }
                }
              }
            ]
          },
                  {
          "replyId": 0,
          "articleId": 0,
          "contents": "연수원 한번도 못가봄...",
          "wdate": "string",
          "user": {
            "uerId": "string",
            "nickname": "싸피5기-1",
            "profileImage": {
              "imageId": 0,
              "path" : "string"
            }
          },
            "rereplies": [
              {
                "replyId": 0,
                "articleId": 0,
                "contents": "코로나 빨리끝나라...",
                "wdate": "string",
                "user": {
                  "userId": "string",
                    "nickname": "싸피 5기-2",
                    "profileImage": {
                      "imageId": 0,
                      "path": "string"
                  }
                }
              }
            ]
          }
        ]
      },
      articleImage: [
        { src:'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQaPm7sl7w_1HvchpsQG_aJUimYlDuI8e_40Q&usqp=CAU'},
        { src:'https://mblogthumb-phinf.pstatic.net/20160106_102/fulsa_14520422034506QKsh_JPEG/20151216_150534.jpg?type=w2'},
        { src:'https://mblogthumb-phinf.pstatic.net/20160106_275/fulsa_1452042203041VxCHA_JPEG/20151216_150441.jpg?type=w2'},
      ],
              colors: [
          'green',
          'secondary',
          'yellow darken-4',
          'red lighten-2',
          'orange darken-1',
        ],
        cycle: false,
        slides: [
          'First',
          'Second',
          'Third',
          'Fourth',
          'Fifth',
        ],

    }
  },
  methods: {
    getArticle(){
      

      axios.get(`${SERVER_URL}/articles/articleId`)
      .then((res) => {
        this.articleInfo = res
      })
    },
    updateArticle(){
      console.log('update')
    },
    deleteArticle(){
      console.log('delete')
      axios.delete(`${SERVER_URL}/articles/articleId`)
      .then((res) => {
        alert('게시물 삭제 완료')
        this.$route.push({name: 'Feed'})
      })
    }
  },
  watch: {
    group () {
      this.drawer = false
    },
  },
  created() {
    this.getArticle()
  }
}
</script>

<style>

</style>