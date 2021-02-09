<template>
  <div class="modal">
    <v-container class="py-0">
      <v-row>
        <v-col cols='12'>
          <div class="overlay" @click="$emit('close-modal')" ></div>
        </v-col>
        
        <v-col col='12'>
          <div class="modal-card">

            <v-text-field 
              v-model="address" 
              label='주소 검색'
              >
            </v-text-field>

            <v-card>
            <v-list>
              <v-list-item
                v-for="(searchedLocation, i) in searchedLocations.slice(0,9)"
                :key="i"
                @click="goSetLocation(searchedLocation)"
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
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchedLocations: "",
      address: '',
    }
  },
  mounted() {
    window.kakao && window.kakao.maps
      this.addKakaoMapScript();
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
    getLocation(res) {
      const self = this
      var places = new kakao.maps.services.Places();

      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
          self.searchedLocations = result
        }
      };
      places.keywordSearch(res, callback);
    },
    goSetLocation(res) {
      this.$emit('goSetLocation', res)
      this.$emit('close-modal')
      
    }
  },
  created() {
  },
  watch: {
    address: function(res) {
      this.getLocation(res)
    }
  },
}

</script>

<style scoped>
.modal {
  z-index: 10;
}
.overlay { 
  width: 100%; 
  height: 100%; 
  position: fixed; 
  left: 0; 
  top: 0; 
} 
.overlay { 
  opacity: 3; 
  background-color: black; 
} 
.modal-card { 
  position: relative; 
  width: 100%; 
  background-color: white; 
}
</style>