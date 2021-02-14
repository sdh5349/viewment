<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
      <v-card
        class="mx-auto mt-5"
        flat
      >
        <v-list>
          <v-list-item
            v-for="(History,i) in Historys"
            :key="i"
            @click="goPage(History)"
          >
            <v-list-item-avatar>
              <v-img
                v-if="History.HistoryImage"
                :src="profileImageUrl+History.HistoryImage"
              >
              </v-img>
              <div v-else>
              <v-icon
                v-if="History.HistoryProperty === 'User'"
                color="primary"
                size="85"
              >
                {{History.HistoryIcon}}
              </v-icon>
              <v-icon
                v-else
              >
                {{History.HistoryIcon}}
              </v-icon>
              </div>
            </v-list-item-avatar>

            <v-list-item-content>
              <v-list-item-title >{{History.HistoryTitle}}</v-list-item-title>
            </v-list-item-content>

            <v-list-item-icon>
              <v-icon
                @click="removeHistory(i)"
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
// import { params } from 'vee-validate/dist/types/rules/alpha';
// import { params } from 'vee-validate/dist/types/rules/alpha';
const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return {
      Historys: {},
      profileImageUrl: `${SERVER_URL}/images/`,
      // removedHistory: {},
    }
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
      var str = localStorage.getItem("Historys");
      // console.log(str)
      var obj = {};
      // console.log(obj)
      try {
        obj = JSON.parse(str);
        // console.log(obj)
      } catch {
        obj = {};
        // console.log(obj)
      }
      if(!obj){
        obj = {};
        // console.log(obj)
        obj["Historys"] = [];
        // console.log(obj)
      }
      this.Historys = obj["Historys"].reverse()
      // this.Historys = obj["Historys"]
      // console.log(this.Historys)
      // console.log(typeof(this.Historys))
    },
    removeHistory(i) {
      // var getLocalStorage = localStorage.getItem("Historys")
      // console.log(this.Historys)
      // console.log(typeof(this.Historys))
      // const delHistorys = this.Historys
      // this.Historys.reverse()
      var recordLength = Object.keys(this.Historys).length - 1
      console.log(recordLength)
      console.log(i)
      console.log(this.Historys)
      console.log(this.Historys[recordLength - i])
      this.Historys.splice(this.Historys[recordLength - i], 1)
      // this.Historys.splice(this.Historys[i], 1)
      console.log(this.Historys)
      // this.obj = delete delHistorys.History
      // console.log(this.Historys)
      // console.log(typeof(this.Historys))
      
      // getLocalStorage.Historys.splice(History,1)
      // localStorage.setItem("Historys", (JSON.stringify(this.Historys)))
      // console.log(JSON.parse(localStorage.getItem("Historys")))
      
      // this.getSearchedlog(this.Historys)
    },
    goPage(History) {
      if (History.HistoryProperty === "User") {
        // this.appendToStorage(History)
        this.$router.push({ name: 'Profile', params: { profileUserId : History.HistoryContent }})
      }
      else if (History.HistoryProperty === "Map") {
        // this.appendToStorage(History)
        this.$router.push({ name: 'Feed', params: {lng: History.HistoryContent, lat: History.HistoryContent2} })
      }
      else if (History.HistoryProperty === "Hashtag") {
        // this.appendToStorage(History)
        this.$router.push({name: 'SearchHashtagGrid', params: {clickedHash: History.HistoryTitle}})
      }
    },
    // appendToStorage(Historys) {
    //   var str = localStorage.getItem("Historys");
    //   var obj = {};
    //   var limitMax = 6;
    //   try {
    //     obj = JSON.parse(str);
    //   } catch {
    //     obj = {};
    //   }
    //   if(!obj){
    //     obj = {};
    //     obj["Historys"] = [];
    //   }
    //   obj["Historys"].push(Historys);
    //   if (limitMax && limitMax < obj["Historys"].length) {
    //     let tempList = [];
    //     for(let i = obj["Historys"].length-limitMax; i < obj["Historys"].length; i++) {
    //       tempList.push(obj["Historys"][i]);
    //     }
    //     obj["Historys"] = tempList;
    //   }
    //   localStorage.setItem("Historys", JSON.stringify(obj));
    // },
  },
  created() {
    this.getSearchedlog()
  },
  watch: {
    // search: function() {
    //   if (this.onTab===0){
    //     console.log("맵a")
    //     this.getSearchedlog()  
    //   }          
    // },
    // search: function() {
    //   console.log("기록a")
    //   if(this.onTab===0){
    //     this.getSearchedlog()
        
    //   }
    // },
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