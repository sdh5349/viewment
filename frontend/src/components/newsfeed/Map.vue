<template>
  <v-row 
    class="map-container"
  >
    <v-col class="py-0">
      <!-- 내 위치로 이동, 기억하기 핀 그리고 기억하기로 이동 버튼들 (시작) -->
      <v-btn-toggle>

        <!-- 내 위치로 이동 버튼 (시작) -->
        <v-btn icon color="black" @click="moveMyLocation" position: absolute style="z-index: 1; top:7px;">
          <v-icon>
            mdi-apple-safari
          </v-icon>
        </v-btn>
        <!-- 내 위치로 이동 버튼 (끝) -->
  
        <!-- 기억하기 핀 버튼 (시작) -->
        <v-btn icon color="black" @click="checkMemory" class="d-flex" position: absolute
          style="z-index: 1; top:54px;">
          <v-icon>
            mdi-pin
          </v-icon>
        </v-btn>
        <!-- 기억하기 핀 버튼 (끝) -->  
  

        <!-- 기억하기로 이동 관련 (시작) -->
        <v-dialog v-model="moveMemoryDialog" scrollable>
          <template v-slot:activator="{ on, attrs }">

            <!-- 기억하기로 이동 버튼 (시작) -->
            <v-btn icon color="black" class="d-flex" v-bind="attrs" v-on="on" position: absolute
              style="z-index: 1; top:100px;">
              <v-icon>
                mdi-book
              </v-icon>
            </v-btn>
            <!-- 기억하기로 이동 버튼 (끝) -->
            
          </template>

          <!-- 기억하기로 이동 버튼 누르면 기억하기를 선택할수있는 카드창 (시작) -->
          <v-card >
            <v-card-title>기억하기 선택</v-card-title>
            <v-divider></v-divider>
            <v-card-text style="height: 300px;" class="pa-1">
              <v-list dense>
                <v-list-item-group color="primary">
                  <v-list-item class="pa-2" v-for="(memory, i) in myMemories" :key="i">                  
                    <v-list-item-content>
                      <v-list-item-title v-text="memory.name" @click="moveMemory(memory)"></v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
  
  
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
              <v-btn text @click="moveMemoryDialog = false">
                닫기
              </v-btn>
  
            </v-card-actions>
          </v-card>       
          <!-- 기억하기로 이동 버튼 누르면 기억하기를 선택할수있는 카드창 (끝) -->
        </v-dialog>
        <!-- 기억하기로 이동 관련 (끝) -->

      </v-btn-toggle>
      <!-- 내 위치로 이동, 기억하기 마커 그리고 기억하기로 이동 버튼들 (끝) -->
  
      <div id="map" class="map"></div>
  
  
      <!-- 기억하기핀 버튼을 누르고 map을 누르면 기억하기를 저장할때 이름과 반경을 정할수 있는 카드 (시작) -->

      <v-row justify="center">
        <v-dialog v-model="saveMemoryDialog" persistent >
          <template v-slot:activator="{ on, attrs }" v-if="pinInfo">
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
              기억하기
            </v-btn>
          </template>
  
  <!-- v-slot="{ invalid }" -->
          <v-card >
            <v-card-title>기억하기 저장</v-card-title>
            <v-divider></v-divider>

              <validation-observer
                ref="observer"
                v-slot="{ invalid }"
              >
              <v-card-text style="height: 300px;" class="pa-1">

              
                  <validation-provider
                    mode="aggressive"
                    rules="required|max:10"
                    v-slot="{ errors }"
                  >
                  <v-text-field 
                    block
                    v-model="memoryName" 
                    label='기억하기 이름' 
                    @click="resetMemoryName"
                    :error-messages="errors"
                    ></v-text-field>
                  </validation-provider>

                  <validation-provider
                    mode="aggressive"
                    rules="required|max_value:1500"
                    v-slot="{ errors }"
                  >
                  <v-text-field 
                    block
                    v-model="memoryRadius" 
                    label='반경(m)' 
                    @click="resetMemoryRadius"
                    :error-messages="errors"
                  ></v-text-field>
                  </validation-provider>
                  </v-card-text>
                  <v-divider></v-divider>
                  <v-card-actions > 
                    
                    <v-col cols="6" >
                    <v-btn  
                      @click="saveMemoryDialog = false" 
                      text 
                    >닫기</v-btn>
                    </v-col>

                    <v-col cols="6" class="text-right">
                    <v-btn 
                      @click='saveMemory' 
                      text 
                      color='primary'
                      :disabled="invalid"
                    >기억 저장</v-btn>
                    </v-col>
                  
                  </v-card-actions>
                  </validation-observer>
                
              
            
            
          </v-card>
        </v-dialog>
      </v-row>
      <!-- 기억하기핀 버튼을 누르고 map을 누르면 기억하기를 저장할때 이름과 반경을 정할수 있는 카드 (끝) -->
  

      <!-- 기억되있는 핀을 누르고 나오는 삭제 오버레이를 누르면 나오는 삭제 확인 카드 (시작) -->
      <v-dialog v-model="deleteMemoryDialog" persistent max-width="290">
        <v-card>
          <v-card-title class="headline">
            정말로 {{ delMemoryName }} 을/를 삭제 하시겠습니까?
          </v-card-title>
          <v-card-text>
            <v-col cols='12'>
              <div class="modal-card">
                <v-btn @click='deleteMemory(delMemoryPinId)' text color='primary'>삭제</v-btn>
                <v-btn @click="deleteMemoryDialog = false" text color="red">닫기</v-btn>
              </div>
            </v-col>
          </v-card-text>
        </v-card>
      </v-dialog>
      <!-- 기억되있는 핀을 누르고 나오는 삭제 오버레이를 누르면 나오는 삭제 확인 카드 (끝) -->

    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios' // back에 axios 요청을 위한 라이브러리
import { required, min, max, max_value } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider } from 'vee-validate'
const SERVER_URL = process.env.VUE_APP_SERVER_URL  // 기본 서버 URL 제일 위의 파일보면 .env파일에 해당
extend('required', {
  ...required,
  message: '필수 입력 항목입니다.'
})
extend('max', {
  ...max,
  message: '기억하기 이름은 10글자 이하이어야 합니다.'
})
extend('max_value', {
  ...max_value,
  message: '기억하기 반경은 1500m 이하이어야 합니다.'
})


export default {
  name: 'Map',
  components: {
    ValidationProvider,
    ValidationObserver,
  },
  props: {
    searchedCenterPosition: Object // 검색하기에서 넘어온 중심 좌표 뉴스피드(부모)에서 내려받은 변수
  },
  data() {
    return{
      map: '', // 지도를 남을 변수 data에 선언해줘야 다른 method에서도 사용가능
      addressName: '', // 지도의 주소를 남을 변수 기억하기에 담을 주소에 사용됨
      is_Memoryshow: false, // 기억을 저장할때 dialog가 보일지 말지를 결정하는 변수
      myLocation: '', // 내 위치로 이동하기 버튼에서 사용되는 내위치 정보를 담을 변수
      pinInfo: '', // 핀 정보를 담는 변수 마커가 찍혀 있는지 아닌지를 판단할때 사용
      container: '', // 지도를 담을 컨테이너에 해당하는 변수 지도 만들때 사용
      options: '', // 지도의 옵션을 담을 변수 중심좌표와 확대 정도가 들어간다. 지도 만들대 사용
      position: '', // 위치 정보를 담을 변수로 사용한다. 
      pins: [], // 게시물 핀들을 담을 배열
      checkMemoryState: false, // 기억하기 핀 버튼을 눌렀는지 안눌렀는지를 판단하는 변수
      is_customOverlay: false, // 커스텀오버레이가 보일지 말지를 판단하는 것  클릭할때마다 보이거나 안보이거나 
      articles: '', // 게시물들을 담을 변수
      memoryName: '기억장소', // 기억하기를 저장할때 장소 이름을 담을 변수
      memoryRadius: '500', // 기억하기를 저장할때 반경을 담을 변수
      myMemories: '', // 지금까지 저장된 기억하기들을 담을 변수
      moveMemoryDialog: false, // 기억하기로 이동하기 dialog를 보여줄지 말지를 판단하는 변수
      saveMemoryDialog: false, // 기억하기를 저장할때 dialog를 보여줄지 말지를 판단하는 변수
      deleteMemoryDialog: false, // 기억하기를 삭제할때 dialog를 보여줄지 말지를 판단하는 변수
      delMemoryPinId: '', // 기억하기 삭제시에 삭제할 pinId를 담을 변수
      delMemoryName: '', // 기억하기 삭제시에 삭제할 pin의 이름을 담을 변수
      memoryInfo: { // 기억하기 정보들을 담을 변수
        name: '',
        radius: '',
        lat: '',
        lng: ''
      },
      centerPosition: { // 지도의 중심좌표를 담을 변수
        lat: '',
        lng: '',
      },
      imageServerPrefix: `${SERVER_URL}/images/`, // 이미지를 쉽게 불러오기 위한 변수

    }
  },
  mounted() {
    window.kakao && window.kakao.maps
      ? this.initMap() // 참
      : this.addKakaoMapScript(); // 거짓
  },
  methods: {
    // 미리 선언한 kakao가 없을떄 실행하는 method
    addKakaoMapScript() {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap)
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=41dd8e1c2fab039d8dbbff2e13e8d5a5&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
      
    },
    initMap() {
      const self = this // 카카오 api에서 this가 안되는 경우가 있어서 대신 self를 사용
      // 검색으로 들어온것인지 뉴스피드 버튼을 눌러서 들어온건지를 판단.
      // self.searchedCenterPosition 요게 검색에 의한 중심좌표 정보
      if (self.searchedCenterPosition.lat){ // 검색한 경우 일떄
        const temp = self.searchedCenterPosition // 검색 중심좌표를 잠시 temp변수에 담아주고
        // temp에 담은 이유는 아래의 업데이트 method에 들어갈 argument의 이름이 너무 길어져서 
        self.updateCenterPosition(temp.lat, temp.lng) // 중심좌표를 업데이트 해준다.
        self.createMap()
      }
      else{ // 뉴스피드 버튼을 눌러서 일때 
        
        if (navigator.geolocation) {
          self.$getLocation()
          .then(coordinates => {
            self.centerPosition = coordinates
            self.createMap()
          })
        }
        else {
          self.centerPosition = { // 센터 좌표를 직접 설정해준다. 지금은 대전 자취방 좌표
            lat : 36.3586873,
            lng : 127.30278400
          }
          self.createMap()
        }
      }
    },
    createMap() {
      const self = this
      self.options = { // 좌표 옵션 센터 + 확대 레벨
          center: new kakao.maps.LatLng(self.centerPosition.lat, self.centerPosition.lng),
          level: 5 
      }
      self.container = document.getElementById("map") // 맵을 가져와서 container변수에 넣어줌
      // 카카오 api를 이용하여 map을 만들고 변수에 넣어줌 이때 map을 담을 container와 option이 필요함
      self.map = new kakao.maps.Map(self.container, self.options) 



      // 주소를 구하기 위한 부분 (시작)
      var geocoder = new kakao.maps.services.Geocoder() // geocoder는 좌표를 주소로 변환하는 툴
      var callback = function(result, status) { // 이 콜백함수는 result는 주소 결과값 state는 제대로 진행 됬는지
        if (status === kakao.maps.services.Status.OK) { // 진행됬으면 안될때는 좌표이상할떄
            self.addressName = result[0].address_name // 구한 주소값을 주소변수에 넣어준다.
        }
      }
      geocoder.coord2RegionCode(self.centerPosition.lng, self.centerPosition.lat, callback) // 함수 실행
      // 주소를 구하기 위한 부분 (시작)
      
      // 중심이 바뀔때의 eventlistner 
      kakao.maps.event.addListener(self.map, 'center_changed', function() {
        const latlng = self.map.getCenter() // 중심좌표가 바뀔때의 중심좌표를 구해서 변수에 넣어주고
        self.updateCenterPosition(latlng.Ma, latlng.La) // 중심좌표를 업데이트 해줌
      })
      // 지도를 클릭했을때의 eventlistner
      kakao.maps.event.addListener(self.map, 'click', function(mouseEvent) {
        self.mapClick(mouseEvent) // 맵 클릭 method를 실행할때 마우스 이벤트를 보내준다.
      })

    },
    // 중심좌표를 업데이트 해주는 method argument값으로 lat(위도)와 lng(경도)가 들어간다.
    updateCenterPosition(lat, lng) {
      this.centerPosition = { // 중심좌표변수에 위도와 경도를 할당하여 업데이트 해준다.
        lat: lat,
        lng: lng
      }
    },
    // 맵클릭 이벤트(기억하기를 위한 이벤트)
    mapClick(mouseEvent) {
        const self = this
        if (self.checkMemoryState){  
          
          var latlng = mouseEvent.latLng;   
          self.position = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng())
          var pin = new kakao.maps.Marker({
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

      self.myMemories.forEach( myMemory => {
        
        var position = new kakao.maps.LatLng(myMemory.lat, myMemory.lng)

        var pin = new kakao.maps.Marker({
            map: self.map, 
            position: position,
            title : myMemory.name,
            image : pinImage
        })


        
        var circle = new kakao.maps.Circle({
            center : position,  // 원의 중심좌표 입니다 
            radius: myMemory.radius, // 미터 단위의 원의 반지름입니다 
            fillColor: '#CFE7FF', // 채우기 색깔입니다
            strokeWeight: 1,
            fillOpacity: 0.2  // 채우기 불투명도 입니다  
        }); 


        circle.setMap(self.map)
        

        var content = document.createElement('input')
        var yAnchor = 2.5
        content.type = 'button'
        content.value = `${myMemory.name} 삭제`
        content.style = `background-color: #E80909; 
                         background-size: 100%; 
                         border-radius: 60px; 
                         border: none;
                         width: 100px;
                         height: 30px;
                         cursor: pointer; 
                         color: 	#FFFFFF; 
                         font-size: 10px; 
                         font-weight: bold;
                        `

        content.addEventListener('click', function(){
          
          self.deleteMemoryDialog = true
          self.delMemoryPinId = myMemory.memoryId
          self.delMemoryName = myMemory.name
        })

      
        const customOverlay = new kakao.maps.CustomOverlay({
            position: position,
            yAnchor: yAnchor,
            content: content,    
        })


      kakao.maps.event.addListener(pin, 'click', memoryClickListener(self.map, customOverlay))        

      })

      function memoryClickListener(map, customOverlay) {
          return function() {
            self.is_customOverlay = !self.is_customOverlay
            if (self.is_customOverlay){
              customOverlay.setMap(map)
            }
            else{
              customOverlay.setMap(null)
            }
          }
        }
      
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

      // const imageSrc = require('@/assets/images/pin.png')
      // const imageSize = new kakao.maps.Size(24, 24)

      const imageSrc = 'https://i1.daumcdn.net/dmaps/apis/n_local_blit_04.png'
      const imageSize = new kakao.maps.Size(24, 35)

      const pinImage = new kakao.maps.MarkerImage(imageSrc, imageSize)
      
      
      self.articles.forEach( article => {
        var position = new kakao.maps.LatLng(article.lat, article.lng)
        var pin = new kakao.maps.Marker({
            map: self.map, 
            position: position,
            title : article.addressName,
            image : pinImage
        })


        var content = document.createElement('input');
        var yAnchor = 1.2
        content.type = 'button'


        content.style = `background: url(${self.imageServerPrefix + article.thumbnail.path}) no-repeat;
                        background-size: 100%; 
                        border-radius: 30px; 
                        border: none;
                        width: 100px;
                        height: 100px;
                        cursor: pointer;`

        content.addEventListener('click', function(){
          
          self.$router.push({name: 'BindArticle', params: {
            pinId: article.pinId,
          }})
        })
        var overlay = new kakao.maps.CustomOverlay({
            position: position,
            content: content,
            yAnchor: yAnchor,
            clickable: true,
        })
        kakao.maps.event.addListener(pin, 'click', clickPin(self.map, overlay));


        self.pins.push(pin)      

      function clickPin(map, overlay) {
          return function() {

            if (overlay.getMap()) {
              overlay.setMap(null)
            } else {
              overlay.setMap(map)
            }

          }
      }




      })
      
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
      res.lng= self.position.La
      
      const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"
      const imageSize = new kakao.maps.Size(24, 35)
      const pinImage = new kakao.maps.MarkerImage(imageSrc, imageSize)

      self.position = new kakao.maps.LatLng(self.position.Ma, self.position.La)


      self.saveMemoryDialog = false
      self.memoryInfo.name = self.memoryName
      self.memoryInfo.radius = self.memoryRadius
      self.memoryInfo.lat = self.position.Ma
      self.memoryInfo.lng = self.position.La
      this.$emit('onClick', self.memoryInfo)
    },
    deleteMemory(memoryId) {

      axios.delete(`${SERVER_URL}/memories/${memoryId}`, this.getToken)
      .then((res) => {
        this.$router.go()
      })
      .catch((err) => {
        
        console.log(err)
      })
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
    centerPosition() {
      this.$emit('update', this.centerPosition)
    }
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
  height: 80vh;
}

.map {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 0;
  top: 0;
  margin: 0;
  
}
.memory-location-modar {
  position: absolute;
  top: 0;
  left: 0;
}

</style>