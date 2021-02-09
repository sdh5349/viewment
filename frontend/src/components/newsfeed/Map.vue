<template>
  <v-row>

    <v-btn icon color="primary" @click="moveMyLocation" position: absolute style="z-index: 9999">
      <v-icon>
        mdi-apple-safari
      </v-icon>
    </v-btn>

    <v-btn icon color="black" @click="checkMemory" class="d-flex" position: absolute style="z-index: 9999; left: 30px">
      <v-icon>
        mdi-pin
      </v-icon>
    </v-btn>







    <div id="map" class="map" position: fixed style="z-index: 0">
    </div>

    <!-- dialog -->
    <v-row justify="center">
      <v-dialog v-model="dialog" persistent max-width="290">
        <template v-slot:activator="{ on, attrs }">
          <v-btn color="primary" dark v-bind="attrs" v-on="on">
            기억하기
          </v-btn>
        </template>

        <v-card>
          <v-card-title class="headline">
            기억할 장소의 이름과 반경을 적어주세요
          </v-card-title>
          <v-card-text>
            <v-col cols='12'>
              <div class="modal-card">
                <v-text-field v-model="memoryName" label='기억하기 이름' @click="resetMemoryName"></v-text-field>

                <v-text-field v-model="memoryRadius" label='반경(km)' @click="resetMemoryRadius"></v-text-field>
                <v-btn @click='memoryClick' block color='primary'>기억 저장</v-btn>
              </div>
            </v-col>
          </v-card-text>
        </v-card>
      </v-dialog>
    </v-row>

  </v-row>
</template>

<script>
import SearchFeedLocation from "../newsfeed/SearchFeedLocation.vue" 
import MemoryLocation from "./MemoryLocation.vue"
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL 

export default {
  props: [
    'goMemoryInfo',
    'myMemories'
  ],
  components: {
    
  },
  data() {
    return{
      addressName: '',
      is_Memoryshow: false,
      map: '',
      roadview: '',
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
      dialog: false,
      memoryName: '',
      memoryRadius: '',
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
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=41dd8e1c2fab039d8dbbff2e13e8d5a5&libraries=services,clusterer,drawing";
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
      ////////

      // 주소 넣기
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            self.addressName = result[0].address_name
        }
      }
    
      geocoder.coord2RegionCode(127.30278400, 36.3586873, callback)
    





      self.alreadyMemoryMarker()
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
      self.mapClick(mouseEvent)

      })
    },
    
    // 맵클릭 이벤트(기억하기를 위한 이벤트)
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
          console.log(self.markerInfo)
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
    // 내 위치로 이동
    moveMyLocation() {
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
    // 기억하기를 위해서 핀을 찍을때 마커를 눌러야 핀을 찍을 수있는데 
    // 핀을 누른 상태인지 아닌지를 판단하는것
    checkMemory() {
      this.checkMemoryState = !this.checkMemoryState
      if (this.markerInfo){
        this.markerInfo.setMap(null)
      }
    },
    // 기억 되있는 마커 찍어 놓기
    alreadyMemoryMarker() {
      const self = this
      const imageSrc = require('@/assets/images/flag2.png')
      const imageSize = new kakao.maps.Size(35, 35)
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
    // 게시물들 받아오기
    getArticle() {
      const self = this
      axios.get(`${SERVER_URL}/pins`, this.getToken)
      .then((res)=> {
        this.articles = res.data
        this.articleMarkers()
      }) 
    },
    // 게시물들 마커 찍기
    articleMarkers() {   
      const self = this

      self.is_articles = !self.is_articles

      // const imageSrc = require('@/assets/images/pin.png')
      // const imageSize = new kakao.maps.Size(24, 24)

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
        this.markers.push(marker)
      }
      function articleMarkerClick(id) {
        return function() {
          self.$router.push({name: 'BindArticle', params: {
            pinId: id,
          }})
        }
      }
      var clusterer = new kakao.maps.MarkerClusterer({
      map: self.map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
      averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
      minLevel: 8 // 클러스터 할 최소 지도 레벨 
      })
      clusterer.addMarkers(self.markers)
    },
    // 기억하기 저장
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
      // this.markers.push(marker)    
      this.$emit('onClick', res)
    },
    resetMemoryName() {
      
    },
    resetMemoryRadius() {

    },
    // 기억하기 장소로 이동
    moveMemory() {
      const self = this
      console.log(self.goMemoryInfo)
      self.map.setCenter(new kakao.maps.LatLng(self.goMemoryInfo.lat, self.goMemoryInfo.lng))
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
        self.mapClick(mouseEvent)
      })
    },
    memoryClick() {
      
    }
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
    },
  },
  // watch: {
  //   markers() {
  //     this.articleMarkers
  //   }
  // }

}
</script>

<style scoped>
.map {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 0;
}
.memory-location-modar {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>