<template>
  <div>
    <v-container class="py-0">
      <v-row >
        <v-col cols='12'>
          <v-text-field 
            @click='searchLocationModal' 
            v-model="address" 
            label='주소 검색'>
          </v-text-field>
        </v-col>

        <v-col cols='12'>
          <SearchArticleLocation 
            v-if="is_show"
            @close-modal="is_show=false"
            @goSetLocation="searchAddress"
            >
          </SearchArticleLocation>
        </v-col>

        <v-col cols='12'>{{ addressName }}</v-col>
  
        <v-col cols='12'>
          <div id="map" class="map"></div>
        </v-col>
    
      </v-row>
    </v-container>
  </div>
</template>

<script>
import SearchArticleLocation from "./SearchArticleLocation.vue" 




export default {
  components: {
    SearchArticleLocation,
  },
  props: {
    visible: {
    type: Boolean,
    require: true,
    default: false
    },   
  },
  data() {
    return{
      myLocation: '',
      addressName: '',
      address: '',
      markerInfo: '',
      container: '',
      options: '',
      map: '',
      position: '',
      marker: '',
      coordinates: '',
      message: '',
      is_show: false,
    }
  },
  mounted() {

    window.kakao && window.kakao.maps
      ? this.initMap()
      : this.addKakaoMapScript();
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
    initMap() {
      const self = this
      self.options = {
        
        center: new kakao.maps.LatLng(36.3586873, 127.30278400),
        level: 5
      }
      self.container = document.getElementById("map")
      self.map = new kakao.maps.Map(self.container, self.options)

      
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            self.addressName = result[0].address_name
        }
      }
      geocoder.coord2RegionCode(127.30278400, 36.3586873, callback)


      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
      self.mapClick(mouseEvent)
      })
    },
    searchAddress(res) {
      const self = this
      self.address = res.place_name
      var places = new kakao.maps.services.Places()

      self.coordinates = new kakao.maps.LatLng(res.y, res.x)
      self.map.setCenter(new kakao.maps.LatLng(self.coordinates.Ma, self.coordinates.La))

      // 주소 넣기
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            self.addressName = result[0].address_name
        }
      }
      geocoder.coord2RegionCode(self.coordinates.La, self.coordinates.Ma, callback)


      // 클릭 이벤트
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
      self.mapClick(mouseEvent)
      })
    },
    mapClick(mouseEvent) {
      const self = this
      if (self.markerInfo !=''){
        self.markerInfo.setMap(null)
      }
      var latlng = mouseEvent.latLng
      self.coordinates = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng())
      self.marker = new kakao.maps.Marker({
        map: self.map,
        position: self.coordinates
      })
      self.markerInfo = self.marker      
      
      // 주소 넣기
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            self.addressName = result[0].address_name
        }
      }
      geocoder.coord2RegionCode(self.coordinates.La, self.coordinates.Ma, callback)

      const markers = {
        Ma: this.coordinates.Ma,
        La: this.coordinates.La,
        addressName: this.addressName
      }
      this.$emit('onClick', markers)
    },
    searchLocationModal() {
      this.is_show = !this.is_show
    },
  }
}
</script>

<style scoped>
.map {
  width: 100%;
  height: 300px;
  z-index: 0;
}
</style>