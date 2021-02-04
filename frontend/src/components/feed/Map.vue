<template>
  <div>
    <v-text-field 
      @click='searchLocationModal' 
      v-model="address" 
      label='주소 검색'
      >
    </v-text-field>

    <SearchArticleLocation
      v-if="is_show"
      @close-modal="is_show=false"
      @goSetLocation="searchAddress"
      >
    </SearchArticleLocation>



    <v-row>

      <v-col>
        <v-btn > 
          <v-icon>
            mdi-cog-outline
          </v-icon>
        </v-btn>
      </v-col>

      <v-col>
        <v-btn @click="moveLocation"> 
          <v-icon>
            mdi-apple-safari
          </v-icon>
        </v-btn>
      </v-col>
      
      <v-col>
        <v-btn @click="checkMemory"> 
          <v-icon>
            mdi-pin
          </v-icon>
        </v-btn>
      </v-col>

    </v-row>

    <!-- {{ addressName }} -->
    <div id="map" class="map"></div>
    <div id="result"></div>
    <v-btn @click="markerCheck(position)">기억 완료</v-btn>
    <MemoryLocation
      v-if="is_Memoryshow"
      @close-Memorymodal="is_Memoryshow=false"
      @onMemory='saveMemory'
      >

    </MemoryLocation>
    




  </div>
</template>

<script>
import SearchArticleLocation from "../feed/SearchArticleLocation.vue" 
import MemoryLocation from "./MemoryLocation.vue"

export default {
  props: [
    'goMemoryInfo',
    'myMemories'
  ],
  components: {
    SearchArticleLocation,
    MemoryLocation
  },
  data() {
    return{
      addressName: '',
      is_show: false,
      is_Memoryshow: false,
      map: '',
      myLocation: '',
      address: '',
      markerInfo: '',
      container: '',
      options: '',
      position: '',
      markers: [],
      checkMemoryState: false,
      is_infowindow: false,
      
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
          level: 3 
      }
      self.container = document.getElementById("map")
      self.map = new kakao.maps.Map(self.container, self.options)
      
      
      self.alreadyMarker()
      // 주소 넣기
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            self.addressName = result[0].address_name + result[0].code
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

      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
      self.mapClick(mouseEvent)
      })

    },
    mapClick(mouseEvent) {
        const self = this
        if (self.checkMemoryState){  
          var latlng = mouseEvent.latLng;   
          self.position = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng())
          var marker = new kakao.maps.Marker({
            map: self.map,
            position: self.position
          })        
          // 이미 마커가 있으면 없어고 찍게 만들기 위한 if문
          if (self.markerInfo==''){
            marker.setMap(self.map)
          } else {
            self.markerInfo.setMap(null)
            marker.setMap(self.map)
          }
          self.markerInfo = marker 

        // 주소 넣기
        var geocoder = new kakao.maps.services.Geocoder()
        var callback = function(result, status) {
          if (status === kakao.maps.services.Status.OK) {
              self.addressName = result[0].address_name
          }
        }
        geocoder.coord2RegionCode(self.position.La, self.position.Ma, callback)
      }
    },
    markerCheck(res) {
      this.is_Memoryshow = !this.is_Memoryshow
    },
    moveLocation() {
      const self = this
      self.$getLocation()
      .then(coordinates => {
        self.myLocation = coordinates
      })
      .then(() => {
        self.map.setCenter(new kakao.maps.LatLng(self.myLocation.lat, self.myLocation.lng))
        kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
          self.mapClick(mouseEvent)
        })
      })
    },
    checkMemory() {
      this.checkMemoryState = !this.checkMemoryState
      if (this.markerInfo){
        this.markerInfo.setMap(null)
      }
    },
    alreadyMarker() {
      const self = this
      const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"
      const imageSize = new kakao.maps.Size(24, 35)
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize)
      for (var i = 0; i < self.myMemories.length; i ++) {
        
        self.position = new kakao.maps.LatLng(self.myMemories[i].lat, self.myMemories[i].lng)
        var marker = new kakao.maps.Marker({
            map: self.map, 
            position: self.position,
            title : self.myMemories[i].name,
            image : markerImage
        })

        var infowindow = new kakao.maps.InfoWindow({
            content: marker.Fb // 인포윈도우에 표시할 내용
        })

        kakao.maps.event.addListener(marker, 'click', makeOverListener(self.map, marker, infowindow))
        
      }
      function makeOverListener(map, marker, infowindow) {
            return function() {
              this.is_infowindow = !this.is_infowindow
              if (this.is_infowindow){
                infowindow.open(map, marker)
              }
              else{
                infowindow.close()
              }
            };
        }
    },
    searchLocationModal(res) {
      this.is_show = !this.is_show
    },
    saveMemory(res) {
      const self = this
      this.is_Memoryshow = !this.is_Memoryshow
      res.lat= this.position.Ma
      res.lng= this.position.La
      
      const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"
      const imageSize = new kakao.maps.Size(24, 35)
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize)

      self.position = new kakao.maps.LatLng(res.lng, res.lat)
      var marker = new kakao.maps.Marker({
          map: self.map, 
          position: self.position,
          title : res.name,
          image :markerImage
      })
      this.markers.push(marker)    
      this.$emit('onClick', res)
    },
    moveMemory() {
      const self = this
      console.log(self.goMemoryInfo)
      self.map.setCenter(new kakao.maps.LatLng(self.goMemoryInfo.lat, self.goMemoryInfo.lng))
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
        self.mapClick(mouseEvent)
      })
    },

  },
  watch: {
    goMemoryInfo: function () {
      this.moveMemory()
    },
    // markers: function () {
      
    //   for (var i = 0; i < 3; i++) {
        

    //     // console.log(this.markers[i].Fb)
    //     // var infowindow = new kakao.maps.InfoWindow({
    //     // content: this.markers[i].Fb // 인포윈도우에 표시할 내용
    //     // }
    //     // this.markers[i].setMap(this.map)
    //   }
                
      
    // }
  }
}
</script>

<style>
.map {
  width: 100%;
  height: 400px;
  z-index: 0; 
}
</style>