<template>
  <v-row class="map-container">
    <v-col>
      <v-btn-toggle>
        <v-btn icon color="black" @click="moveMyLocation"  position: absolute style="z-index: 9999; top:7px;">
          <v-icon>
            mdi-apple-safari
          </v-icon>
        </v-btn>

      
        <v-btn icon color="black" @click="checkMemory" class="d-flex" position: absolute style="z-index: 9999; left: 50px; top:7px;">
          <v-icon>
            mdi-pin
          </v-icon>
        </v-btn>
      

    <v-dialog
      v-model="moveMemoryDialog"
      scrollable
      max-width="300px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn icon 
          color="black" 
          class="d-flex" 
          v-bind="attrs"
          v-on="on"
          position: absolute 
          style="z-index: 9999; left: 99px; top:7px;">
          <v-icon>
            mdi-book
          </v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title>기억하기 선택</v-card-title>
        <v-divider></v-divider>
        <v-card-text style="height: 300px;">
          <v-list dense>
            <v-list-item-group
              v-model="selectMemory"
              color="primary"
              > 
                <v-list-item
                  v-for="(memory, i) in myMemories"
                  :key="i"
                  >
                <!-- <v-list-item-icon>
                  <v-icon v-text="item.icon"></v-icon>
                </v-list-item-icon> -->
                <v-list-item-content>
                  <v-list-item-title v-text="memory.name" @click="moveMemory(memory)"></v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>


        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-btn
            color="blue darken-1"
            text
            @click="moveMemoryDialog = false"
          >
            닫기
          </v-btn>

        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-btn-toggle>

      
        <div id="map" class="map"></div>


        <!-- dialog -->
        <v-row justify="center">
          <v-dialog v-model="saveMemoryDialog" persistent max-width="290">
            <template v-slot:activator="{ on, attrs }" v-if="pinInfo">
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
                    <v-btn @click='saveMemory' text color='primary'>기억 저장</v-btn>
                    <v-btn @click="saveMemoryDialog = false" text color="red">닫기</v-btn>
                  </div>
                </v-col>
              </v-card-text>
            </v-card>
          </v-dialog>
        </v-row>
    </v-col>
  </v-row>
</template>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script>
import MemoryLocation from "./MemoryLocation.vue"
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL 

export default {
  props: [
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
      pinInfo: '',
      container: '',
      options: '',
      position: '',
      pins: [],
      checkMemoryState: false,
      is_infowindow: false,
      articles: '',
      is_articles: false,
      memoryName: '기억장소',
      memoryRadius: '5',
      myMemories: '',
      moveMemoryDialog: false,
      selectMemory: '',
      saveMemoryDialog: false,
      memoryInfo: {
        name: '',
        radius: '',
        lat: '',
        lng: ''
      }
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
      
      
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
      self.mapClick(mouseEvent)

      })
    },
    
    // 맵클릭 이벤트(기억하기를 위한 이벤트)
    mapClick(mouseEvent) {
        const self = this
        if (self.checkMemoryState){  
          console.log(self.checkMemoryState)
          var latlng = mouseEvent.latLng;   
          self.position = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng())
          var pin = new kakao.maps.Marker({
            map: self.map,
            position: self.position
          })        
          // 이미 마커가 있으면 없어고 찍게 만들기 위한 if문
          if (self.pinInfo==''){
            pin.setMap(self.map)
          } else {
            self.pinInfo.setMap(null)
            pin.setMap(self.map)
          }
          self.pinInfo = pin 
          console.log(self.pinInfo)
        // 주소 넣기
        var geocoder = new kakao.maps.services.Geocoder()
        var callback = function(result, status) {
          if (status === kakao.maps.services.Status.OK) {
              self.addressName = result[0].address_name
          }
        }
        geocoder.coord2RegionCode(self.position.La, self.position.Ma, callback)
        self.saveMemoryDialog = true
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
      if (this.pinInfo){
        this.pinInfo.setMap(null)
      }
    },
    // 기억 되있는 마커 찍어 놓기
    alreadyMemoryPin() {
      
      const self = this
      const imageSrc = require('@/assets/images/flag2.png')
      const imageSize = new kakao.maps.Size(35, 35)
      const pinImage = new kakao.maps.MarkerImage(imageSrc, imageSize)
      for (var i = 0; i < self.myMemories.length; i ++) {
        
        self.position = new kakao.maps.LatLng(self.myMemories[i].lat, self.myMemories[i].lng)
        var pin = new kakao.maps.Marker({
            map: self.map, 
            position: self.position,
            title : self.myMemories[i].name,
            image : pinImage
        })
        
    
        const content = `<div @onclick="closeOverlay()"> ` +
                        `dasdas ` +
                        `</div>`


        var customOverlay = new kakao.maps.CustomOverlay({
            position: self.position,
            map: self.map,
            content: content   
        });

      kakao.maps.event.addListener(pin, 'click', function() {
          customOverlay.setMap(null)
          console.log(1322323)  
      });

      function closeOverlay() {
          console.log(123)
          // overlay.setMap(null);     
      }


        // customOverlay.setMap(self.map);
        // kakao.maps.event.addListener(pin, 'click', memoryClickListener(self.map, pin, customOverlay))
        
        
      }


      // function memoryClickListener(map, pin, customOverlay) {
      //     return function() {
      //       this.is_infowindow = !this.is_infowindow
      //       if (this.is_infowindow){
      //         customOverlay.open(map, pin)
      //       }
      //       else{
      //         customOverlay.close()
      //       }
      //     }
      //   }
      
    },
    // 게시물들 받아오기
    getArticle() {
      const self = this
      axios.get(`${SERVER_URL}/pins`, this.getToken)
      .then((res)=> {
        this.articles = res.data
        this.articlePins()
      }) 
    },
    // 게시물들 마커 찍기
    articlePins() {   
      const self = this

      self.is_articles = !self.is_articles

      // const imageSrc = require('@/assets/images/pin.png')
      // const imageSize = new kakao.maps.Size(24, 24)

      const imageSrc = 'https://i1.daumcdn.net/dmaps/apis/n_local_blit_04.png'
      const imageSize = new kakao.maps.Size(24, 35)

      const pinImage = new kakao.maps.MarkerImage(imageSrc, imageSize)
      
      for (var i = 0; i < self.articles.length; i ++) {
        const position = new kakao.maps.LatLng(self.articles[i].lat, self.articles[i].lng)
        var pin = new kakao.maps.Marker({
            map: self.map, 
            position: position,
            title : self.articles[i].address_name,
            image : pinImage
        })
        
        const id = self.articles[i].pinId
        kakao.maps.event.addListener(pin, 'click', articlePinClick(id))
        this.pins.push(pin)

      }



      function articlePinClick(id) {
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
      clusterer.addMarkers(self.pins)
    },
    // 기억하기 저장
    saveMemory(res) {
      
      const self = this
      self.is_Memoryshow = !self.is_Memoryshow
      res.lat= self.position.Ma
      res.lng= this.position.La
      
      const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"
      const imageSize = new kakao.maps.Size(24, 35)
      const pinImage = new kakao.maps.MarkerImage(imageSrc, imageSize)

      self.position = new kakao.maps.LatLng(self.position.Ma, self.position.La)
      var pin = new kakao.maps.Marker({
          map: self.map, 
          position: self.position,
          title: res.name,
          image: pinImage
      })

      self.saveMemoryDialog = false
      self.memoryInfo.name = self.memoryName
      self.memoryInfo.radius = self.memoryRadius
      self.memoryInfo.lat = self.position.Ma
      self.memoryInfo.lng = self.position.La
      this.$emit('onClick', self.memoryInfo)
    },
    deleteMemory() {
      // console.log(123)
      // axios.delete(`${SERVER_URL}/memories/${pin}`)
    },
    resetMemoryName() {
      this.memoryName = ''
    },
    resetMemoryRadius() {
      this.memoryRadius = ''
    },
    // 기억하기 장소로 이동
    moveMemory(memory) {
      const self = this
      self.moveMemoryDialog = false
      self.map.setCenter(new kakao.maps.LatLng(memory.lat, memory.lng))
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
        self.mapClick(mouseEvent)
      })
    },
    getMemories() {
      const userId = sessionStorage.getItem('uid')
      axios.get(`${SERVER_URL}/users/${userId}/memories`, this.getToken)
      .then((res) => {
        this.myMemories = res.data    
        this.alreadyMemoryPin()    
      })
    },
  },
  created() {
    this.getMemories()
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
}
</script>

<style scoped>
.map-container {
  position: relative;
}

.map {
  position: absolute;
  width: 100%;
  height: 70vh;
  z-index: 0;
  margin: 0%;
  
}
.memory-location-modar {
  position: absolute;
  top: 0;
  left: 0;
  
}
</style>