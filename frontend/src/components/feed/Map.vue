<template>
  <div>
    <v-container>
      <v-row >
        <v-col cols='12'>
          <v-text-field 
            @click='searchLocationModal' 
            v-model="address" 
            label='주소 검색'
            >
          </v-text-field>
        </v-col>
        
        <v-col cols="12">
          <SearchArticleLocation
            v-if="is_show"
            @close-modal="is_show=false"
            @goSetLocation="searchAddress"
          ></SearchArticleLocation>
        </v-col>

        <v-col cols='12' >
          <v-btn 
            v-if="is_articles===false"
            block 
            @click='articleMarkers'
            color="primary"
          >게시물 받아오기</v-btn>          
        </v-col>
    </v-row>

    <v-row     
      justify="space-around"
    >
      <v-col cols='4' align="center">
        <v-btn icon color="black"> 
          <v-icon>
            mdi-cog-outline
          </v-icon>
        </v-btn>
      </v-col>

      <v-col cols='4' align="center">
        <v-btn icon color="primary" @click="moveLocation" > 
          <v-icon>
            mdi-apple-safari
          </v-icon>
        </v-btn>
      </v-col>
      
      <v-col cols='4' align="center">
        <v-btn icon color="black" @click="[checkMemory, loader='loading']"> 
          <v-icon>
            mdi-pin
          </v-icon>
        </v-btn>
      </v-col>
    </v-row>


      <div 
        id="map" 
        class="map"
        >
      </div>

    <v-btn 
      v-if="markerInfo != ''" 
      @click="markerCheck(position)"
      block
      color="primary"
      >
      기억 완료
    </v-btn>
    <MemoryLocation
      v-if="is_Memoryshow"
      @close-Memorymodal="is_Memoryshow=false"
      @onMemory='saveMemory'
      >
    </MemoryLocation>
    
      
    </v-container>
  </div>
</template>

<script>
import SearchArticleLocation from "../feed/SearchArticleLocation.vue" 
import MemoryLocation from "./MemoryLocation.vue"
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL 

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
      articles: '',
      is_articles: false,
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
    getArticle() {
      axios.get(`${SERVER_URL}/pins`, this.getToken)
      .then((res)=> {
        this.articles = res.data
      }) 
    },
    articleMarkers() {
      const self = this
      self.is_articles = !self.is_articles

      const imageSrc = 'https://i1.daumcdn.net/dmaps/apis/n_local_blit_04.png'
      const imageSize = new kakao.maps.Size(24, 35)
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize)

      for (var i = 0; i < self.articles.length; i ++) {
        const position = new kakao.maps.LatLng(self.articles[i].lat, self.articles[i].lng)
        var marker = new kakao.maps.Marker({
            map: self.map, 
            position: position,
            title : self.articles[i].address_name,
            image : markerImage
        })
        
        const id = self.articles[i].pinId
        kakao.maps.event.addListener(marker, 'click', articleMarkerClick(id))
    
      }
      function articleMarkerClick(id) {
        return function() {
          self.$router.push({name: 'BindArticle', params: {
            pinId: id,
          }})
        }
      }
    },
    // articleMarkerClick(id) {
    //   console.log(id)
    // },
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

      self.position = new kakao.maps.LatLng(res.lat, res.lng)
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
  created() {
    this.getArticle()
  },
  watch: {
    goMemoryInfo: function () {
      this.moveMemory()
    },
  },
  computed: {
    getToken(){
      const token = sessionStorage.getItem('jwt')
      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
}
</script>

<style>
.map {
  width: 100%;
  height: 400px;
  z-index: 0;
  /* position: absolute;  */
}
</style>