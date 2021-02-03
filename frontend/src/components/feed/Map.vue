<template>
  <div>
    <v-text-field 
      @click='searchLocationModal' 
      v-model="address" 
      label='주소 검색'>
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

    {{ addressName }}
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
import SearchArticleLocation from "../article/SearchArticleLocation.vue" 
import MemoryLocation from "./MemoryLocation.vue"

export default {
  props: [
    'goMemoryInfo'
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
      checkMemoryState: false,
      positions : [
          {
            title: '카카오', 
            latlng: new kakao.maps.LatLng(33.450705, 126.570677)
          },
          {
            title: '생태연못', 
            latlng: new kakao.maps.LatLng(33.450936, 126.569477)
          },
          {
            title: '텃밭', 
            latlng: new kakao.maps.LatLng(33.450879, 126.569940)
          },
          {
            title: '근린공원',
            latlng: new kakao.maps.LatLng(33.451393, 126.570738)
          },
          {
            title: '최애 초밥집',
            latlng: new kakao.maps.LatLng(36.358280, 127.30320581)
          },
            {
            title: '우리 집',
            latlng: new kakao.maps.LatLng(36.3586873, 127.30278400)
          }
      ]
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
      self.alreadyMarker(self.map)
      // 주소 넣기
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            self.addressName = result[0].address_name + result[0].code
        }
      }
      geocoder.coord2RegionCode(127.30278400, 36.3586873, callback)
      
      self.mapClick()
    },
    searchAddress(res) {
      const self = this
      self.address = res.place_name
      var places = new kakao.maps.services.Places()

      self.coordinates = new kakao.maps.LatLng(res.y, res.x)
      self.container = document.getElementById('map') 
        self.options = {
            center: new kakao.maps.LatLng(self.coordinates.Ma, self.coordinates.La), // 지도의 중심좌표
            level: 3 
      }
      self.map = new kakao.maps.Map(self.container, self.options)

      // 주소 넣기
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            self.addressName = result[0].address_name + result[0].code
        }
      }
      geocoder.coord2RegionCode(self.coordinates.La, self.coordinates.Ma, callback)
      // self.alreadyMarker(self.map)
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
      self.mapClick(mouseEvent)
      })

    },
    markerCheck(res) {
      this.is_Memoryshow = !this.is_Memoryshow
    },
    mapClick() {
        
        const self = this
        self.map = new kakao.maps.Map(this.container, this.options)
        kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
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
                self.addressName = result[0].address_name + result[0].code
            }
          }
          geocoder.coord2RegionCode(self.position.La, self.position.Ma, callback)
        }
      })
    },
    moveLocation() {
      const self = this
      self.$getLocation()
      .then(coordinates => {
        self.myLocation = coordinates
      })
      .then(() => {
        self.options = {
          center: new kakao.maps.LatLng(self.myLocation.lat, self.myLocation.lng),
          level: 3 
        }  
        self.map = new kakao.maps.Map(self.container, self.options)
        
        kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
        self.mapClick(mouseEvent)
        })


        // 요거 수정
        // self.alreadyMarker(self.map)
      })
    },
    checkMemory() {
      this.checkMemoryState = !this.checkMemoryState
      if (this.markerInfo){
        this.markerInfo.setMap(null)
      }
    },
    alreadyMarker(map) {
      const self = this
      var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
    
      for (var i = 0; i < this.positions.length; i ++) {
           
        var imageSize = new kakao.maps.Size(24, 35);     
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);    
        var marker = new kakao.maps.Marker({
            map: self.map, 
            position: self.positions[i].latlng,
            title : self.positions[i].title,
            image : markerImage
        })
      }
    },
    searchLocationModal(res) {
      this.is_show = !this.is_show

    },
    saveMemory(res) {
      this.is_Memoryshow = !this.is_Memoryshow
      res.lat= this.position.Ma
      res.lng= this.position.La
      this.$emit('onClick', res)
    },
    moveMemory() {
      const self = this
      self.options = {
        center: new kakao.maps.LatLng(self.goMemoryInfo.lng, self.goMemoryInfo.lat),
        level: 3 
      }  
      self.map = new kakao.maps.Map(self.container, self.options)
        
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
        self.mapClick(mouseEvent)
      })
    },

  },
  watch: {
    goMemoryInfo: function () {
      this.moveMemory()
    }
  }
}
</script>

<style>
.map {
  width: 100%;
  height: 400px;
}
</style>