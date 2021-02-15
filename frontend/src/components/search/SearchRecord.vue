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
            @click="goPage(History)"
          >
            <v-list-item-avatar>
              <UserProfileImage
                v-if="History.HistoryProperty === 'User'"
                :profileImage="History.HistoryImage"
              />
              <v-btn icon v-else>
                <v-icon
                  
                >
                  {{History.HistoryIcon}}
                </v-icon>
              </v-btn>
            </v-list-item-avatar>

            <v-list-item-content>
              <v-list-item-title >{{History.HistoryTitle}}</v-list-item-title>
            </v-list-item-content>

            <v-list-item-icon>
              <v-icon
                @click="removeHistory(History)"
              >
                mdi-close
              </v-icon>
            </v-list-item-icon>
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
      loading: false,
      Historys: [],
      profileImageUrl: `${SERVER_URL}/images/`,
      // removedHistory: {},
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
    getSearchedlog() {
      var record = JSON.parse(localStorage.getItem('Historys'))
      console.log("레코드입니다",typeof(record))
      if (record) {
        this.Historys = record.reverse()
        // this.Historys = record
      }
    },
    removeHistory(History) {
      const targetHistoryIdx = this.Historys.indexOf(History)
      this.Historys.splice(targetHistoryIdx, 1)
      localStorage.setItem("Historys", JSON.stringify(this.Historys))
    },
    goPage(History) {
      if (History.HistoryProperty === "User") {
        // this.appendToStorage(History)
        this.$router.push({ name: 'Profile', params: { profileUserId : History.HistoryContent }})
      }
      else if (History.HistoryProperty === "Map") {
        this.$router.push({ name: 'NewsFeed', params: {lng: History.HistoryContent, lat: History.HistoryContent2} })
      }
      else if (History.HistoryProperty === "Hashtag") {
        // this.appendToStorage(History)
        this.$router.push({name: 'SearchHashtagGrid', params: {clickedHash: History.HistoryTitle}})
      }
    },
  },
  created() {
    console.log("다시되니???")
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