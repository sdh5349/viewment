<template>
  <v-row
    justify="center"
  >
    <v-col>

      <v-card
        class="mx-auto mt-5 padding: 0px 16px"
        flat
      >
     
        <v-list>
          
          <v-list-item
            v-for="(History,i) in Historys"
            :key="i"
          >
            <v-list-item-avatar  @click="goPage(History)">
              <UserProfileImage
                v-if="History.HistoryProperty === 'User'"
                :profileImage="History.HistoryImage"
              />
                <v-icon 
                  large
                  v-else
                >
                  {{History.HistoryIcon}}
                </v-icon>
            </v-list-item-avatar>

            <v-list-item-content @click="goPage(History)">
              <v-list-item-title >{{History.HistoryTitle}}</v-list-item-title>
            </v-list-item-content>

            <v-list-item-action>
              <v-btn
                icon
                @click="removeHistory(History)"
              >
                <v-icon>
                  mdi-close
                </v-icon>
              </v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list>    
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import UserProfileImage from '@/components/user/UserProfileImage'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return {
      Historys: [],
      profileImageUrl: `${SERVER_URL}/images/`,
    }
  },
  components: {
    UserProfileImage,
  },
  props:{
    search : {
      type: String,
    },
    onTab : {
      type: Number,
    }
  },
  methods: {
    // 검색기록
    getSearchedlog() {
      // this.loading = true
      var record = JSON.parse(localStorage.getItem('Historys'))
      if (record) {
        this.Historys = record.reverse()
      }
    },
    // 검색기록 삭제
    removeHistory(History) {
      const targetHistoryIdx = this.Historys.indexOf(History)
      this.Historys.splice(targetHistoryIdx, 1)
      localStorage.setItem("Historys", JSON.stringify(this.Historys))
    },
    // 검색기록 click시 가장 위로(최근검색)으로 올려주고 각 page로 이동
    goPage(History) {
      var tempArray
      tempArray = JSON.parse(localStorage.getItem('Historys'))
      var index = tempArray.findIndex(x => x.HistoryTitle === History.HistoryTitle && x.HistoryProperty === History.HistoryProperty)
      if (index != -1){
        tempArray.splice(index, 1)
      }
      tempArray.push(History)
      localStorage.setItem('Historys', JSON.stringify(tempArray))   
      if (History.HistoryProperty === "User") {
        this.$router.push({ name: 'Profile', params: { profileUserId : History.HistoryContent }})
      }
      else if (History.HistoryProperty === "Map") {
        this.$router.push({ name: 'NewsFeed', params: {lng: History.HistoryContent, lat: History.HistoryContent2} })
      }
      else if (History.HistoryProperty === "Hashtag") {
        this.$router.push({name: 'SearchHashtagGrid', params: {clickedHash: History.HistoryTitle}})
      }
    },
  },
  created() {
    this.loginUserId = sessionStorage.getItem('uid')
    this.getSearchedlog()
  },
  watch: {
    onTab: function() {
      if(this.onTab===0){
        this.getSearchedlog()
      }
    }
  },
}
</script>

<style>

</style>