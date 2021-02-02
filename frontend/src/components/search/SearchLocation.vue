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
            @click="goMap(searchedLocation)"
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
export default {
  data() {
    return {
      searchedLocations: "",
    }
  },
  mounted() {
    window.kakao && window.kakao.maps
      this.addKakaoMapScript();
  },
  props: {
    search : {
      type: String,
    }
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
          console.log(result);
        }
      };
      places.keywordSearch(this.search, callback);
    },
    goMap(searchedLocation) {
      console.log(searchedLocation)
      this.$router.push({ name: 'Feed', params: {id: searchedLocation} })
    }
  },
  created() {
  },
  watch: {
    search: function(res) {
      this.getLocation(res)
    }
  },
}
</script>

<style>

</style>