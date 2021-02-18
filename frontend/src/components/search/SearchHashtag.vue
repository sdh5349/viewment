<template>
  <v-row
  justify="center"
  >
    <v-col>
      <v-card
        class="mx-auto mt-5"
        flat
      >
        <v-list v-if="flag">
          <v-list-item
            v-for="hash in hashtags.slice(0,9)"
            :key="hash.contents"
            @click="goGrid(hash)"
          >
            <v-list-item-avatar>
              <v-icon>
                mdi-pound
              </v-icon>
            </v-list-item-avatar>

            <v-list-item-content>
              <v-list-item-title v-text="hash.contents"></v-list-item-title>
            </v-list-item-content>

          </v-list-item>
        </v-list>
        <p
          v-else
          style="padding-top: 10px; padding-left: 30px"
        >
        </p>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import { extend, ValidationObserver, ValidationProvider } from 'vee-validate' 

const SERVER_URL = process.env.VUE_APP_SERVER_URL

// extend('search', {
//   validate: value => {
//     var flag = false

//     let re = /^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9]*$/i;
//     if(re.test(value))
//       flag = true;

//     return flag;
//   }
// })

export default {
  components: {
      // ValidationProvider,
      // ValidationObserver,
  },
  data() {
    return {
      flag: true,
      hashtags: [],
      Historys: [],
      page: 0,
      size: 200,
      last: false,
    }
  },
  props: {
    search : {
      type: String,
    },
    onTab : {
      type: Number, 
    },
    // limitMax: {
    //   type: Number,
    // },
  },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt')

      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
  methods: {
    // Hashtag 가져오기
    getHashtags() {
      if(this.search){
          let re = /^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9]*$/i;
          if(!re.test(this.search)){
            console.log("딱걸렸어")
            this.flag = false;
            return;
          }
          axios.get(`${SERVER_URL}/hashtags/${this.search}`, this.getToken)
            .then((res) => {
            this.hashtags = res.data
            })
            .catch((err)=> {
              alert('error'+err.message)
            })
      }
    },
    // 그리드로 page로 데이터 포함해 이동
    goGrid(hash) {
      console.log(hash.contents)
      this.$router.push({name: 'SearchHashtagGrid', params: {clickedHash: hash.contents}})
      this.History = 
        {
          HistoryTitle: hash.contents,
          HistoryContent: hash.hashtagId, 
          HistoryIcon:"mdi-pound",
          HistoryProperty: "Hashtag",
        }
      this.appendToStorage(this.History)
    },
    // 검색기록을 위한 localstorage 저장(중복제거)
    appendToStorage(History) {
      var tempArray
      if (localStorage.getItem('Historys') === null) {
        tempArray = [];
      } 
      else {
        tempArray = JSON.parse(localStorage.getItem('Historys'))
      }
      var index = tempArray.findIndex(x => x.HistoryTitle === History.HistoryTitle && x.HistoryProperty === History.HistoryProperty)
      if (index != -1){
        tempArray.splice(index, 1)
      }
      tempArray.push(History)
      localStorage.setItem('Historys', JSON.stringify(tempArray))   
    },
    // 스크롤이 맨 아래에 있고 더 요청할 유저의 정보가 남아있다면 팔로워 정보를 더 요청한다
    scrolling (event) {
      const scrollInfo = event.target
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight && !this.last) {
        this.getHashtags()
      }
    },
  },
  watch: {
    onTab: {
      immediate: true,
      handler(onTab) {
        fetch(`/${onTab}`).then((data) => {
          this.getHashtags()
        })
      }
    },
    search: function() {
      if(this.onTab===2){
        console.log("해쉬a")
        this.getHashtags()       
      }
    },
  },
}
</script>

<style>

</style>