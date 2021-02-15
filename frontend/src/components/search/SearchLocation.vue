<template>
  <v-row
    justify="center"
  >
    <v-col>
      <v-card
        class="mx-auto mt-5"
        flat
      >
        <v-list>
          <v-list-item
            v-for="(searchedLocation, i) in searchedLocations"
            :key="i"
            @click="goMap(searchedLocation,searchedLocation.address_name,searchedLocation.place_name)"
          >
            <v-list-item-avatar>
              <v-icon>
                mdi-map-marker
              </v-icon>
            </v-list-item-avatar>    
            <v-list-item-content>
              <v-list-item-title >{{searchedLocation.place_name}}</v-list-item-title>
              <v-list-item-title >{{searchedLocation.address_name}}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
// import { params } from 'vee-validate/dist/types/rules/alpha';
export default {
  data() {
    return {
      searchedLocations: "",
      History: {},
    }
  },
  mounted() {
    window.kakao && window.kakao.maps
      this.addKakaoMapScript();
  },
  props: {
    search : {
      type: String,
    },
    onTab : {
      type: Number,
    },
  },
  methods: {
      addKakaoMapScript() {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap)
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=41dd8e1c2fab039d8dbbff2e13e8d5a5";
      document.head.appendChild(script);
    },
    getLocation() {
      const self = this
      var places = new kakao.maps.services.Places();

      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
          self.searchedLocations = result
        }
      };
      if(this.search) {
        places.keywordSearch(this.search, callback);
      }
    },
    appendToStorage(History) {
      var tempArray

      if (localStorage.getItem('Historys') === null) {
        tempArray = [];
      } else {
        tempArray = JSON.parse(localStorage.getItem('Historys'))
      }

      tempArray.push(History);
      localStorage.setItem('Historys', JSON.stringify(tempArray));    
    },
    goMap(searchedLocation) {
      this.History = 
        {
          HistoryTitle: searchedLocation.place_name,
          HistoryContent: searchedLocation.x, 
          HistoryContent2: searchedLocation.y, 
          HistoryIcon:"mdi-map-marker",
          HistoryProperty: "Map",
        }
      this.$router.push({ name: 'NewsFeed', params: {lng: searchedLocation.x, lat: searchedLocation.y} })
      this.appendToStorage(this.History)
    },
    scrolling (event) {
      const scrollInfo = event.target
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight && !this.last) {
        this.readMore()
      }
    },
  },
  watch: {
    onTab: {
      immediate: true,
      handler(onTab) {
        fetch(`/${onTab}`).then((data) => {
          this.getLocation()
        })
      }
    },
    search: function() {
      if(this.onTab===1){
        console.log("맵a")
        this.getLocation() 
      }
    },
  },
}
</script>

<style>

</style>