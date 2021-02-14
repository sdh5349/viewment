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
            v-for="(searchedLocation, i) in searchedLocations.slice(0,9)"
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
      Historys: [],
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
    appendToStorage(Historys) {
      var str = localStorage.getItem("Historys");
      var obj = {};
      var limitMax = 6;
      try {
        obj = JSON.parse(str);
      } catch {
        obj = {};
      }
      if(!obj){
        obj = {};
        obj["Historys"] = [];
      }
      obj["Historys"].push(Historys);
      if (limitMax && limitMax < obj["Historys"].length) {
        let tempList = [];
        for(let i = obj["Historys"].length-limitMax; i < obj["Historys"].length; i++) {
          tempList.push(obj["Historys"][i]);
        }
        obj["Historys"] = tempList;
      }
      localStorage.setItem("Historys", JSON.stringify(obj));
    },
    goMap(searchedLocation) {
      this.Historys = 
        {
          HistoryTitle: searchedLocation.place_name,
          HistoryContent: searchedLocation.x, 
          HistoryContent2: searchedLocation.y, 
          HistoryIcon:"mdi-map-marker",
          HistoryProperty: "Map",
        }
      this.$router.push({ name: 'Feed', params: {lng: searchedLocation.x, lat: searchedLocation.y} })
      this.appendToStorage(this.Historys)
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