<template>
  <div>
    <v-text-field 
      @keypress.enter='searchAddress' 
      v-model="address" 
      label='주소 검색'>
    </v-text-field>
    
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


    <div id="map" class="map"></div>
    <div id="result"></div>
    <v-btn @click="markerCheck(position)">기억 완료</v-btn>
  </div>
</template>

<script>
export default {
  data() {
    return{
      myLocation: '',
      address: '',
      markerInfo: '',
      container: '',
      options: '',
      position: '',
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
      var control = new kakao.maps.ZoomControl()
      this.container = document.getElementById("map")
      
      // this.$getLocation()
      // .then(coordinates => {
      //   this.myLocation = coordinates
      // })
      // .then(() => {
      //   this.options = {
      //     //지도를 생성할 때 필요한 기본 옵션
      //     center: new kakao.maps.LatLng(this.myLocation.lat, this.myLocation.lng), //지도의 중심좌표.
      //     level: 3 //지도의 레벨(확대, 축소 정도)
      //   }
        
        
      //   var map = new kakao.maps.Map(this.container, this.options) //지도 생성 및 객체 리턴
      //   self.alreadyMarker(map)
        
      //   // this.mapClick()
      // })
      this.options = {
        //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(36.3586873, 127.30278400), //지도의 중심좌표.
        level: 3 //지도의 레벨(확대, 축소 정도)
      }
      var map = new kakao.maps.Map(this.container, this.options) //지도 생성 및 객체 리턴
    },
    searchAddress() {
      
      const self = this
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
          if (status === kakao.maps.services.Status.OK) {
              console.log(result);
          }
      }
      
      geocoder.addressSearch(self.address, function(result, status) {

    // 정상적으로 검색이 완료됐으면 
      if (status === kakao.maps.services.Status.OK) {
        
        var coords = new kakao.maps.LatLng(result[0].y, result[0].x)
        
        self.container = document.getElementById('map') // 지도를 표시할 div 
            self.options = {
                center: new kakao.maps.LatLng(coords.Ma, coords.La), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
        }
        var map = new kakao.maps.Map(self.container, self.options) //지도 생성 및 객체 리턴
        self.alreadyMarker(map)
        } 
      })    
    },
    markerCheck(res) {
      console.log(this.position)
      this.$emit('onClick', this.position)
    },
    mapClick() {
      
      const self = this
      var map = new kakao.maps.Map(this.container, this.options) //지도 생성 및 객체 리턴
      this.alreadyMarker(map)
      kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
          // 클릭한 위도, 경도 정보를 가져옵니다 
          var latlng = mouseEvent.latLng;
          var message = '기억한 위치의 위도는 ' + latlng.getLat() + ' 이고, '
          message += '경도는 ' + latlng.getLng() + ' 입니다'
          
          var resultDiv = document.getElementById('result')
          resultDiv.innerHTML = message
          
          self.position = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng())
          var marker = new kakao.maps.Marker({
            map: map,
            position: self.position
          })
          
          // 이미 마커가 있으면 없어고 찍게 만들기 위한 if문
          if (self.markerInfo==''){
            marker.setMap(map)
          } else {
            self.markerInfo.setMap(null)
            marker.setMap(map)
          }
          self.markerInfo = marker    
      })
    },
    moveLocation() {
      const self = this
      this.$getLocation()
      .then(coordinates => {
        this.myLocation = coordinates
      })
      .then(() => {
        this.options = {
          //지도를 생성할 때 필요한 기본 옵션
          center: new kakao.maps.LatLng(this.myLocation.lat, this.myLocation.lng), //지도의 중심좌표.
          level: 3 //지도의 레벨(확대, 축소 정도)
        }  
        var map = new kakao.maps.Map(this.container, this.options) //지도 생성 및 객체 리턴
        self.alreadyMarker(map)
      })
    },
    checkMemory() {
      this.mapClick()
    },
    alreadyMarker(map) {
      var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
    
      for (var i = 0; i < this.positions.length; i ++) {
        
        // 마커 이미지의 이미지 크기 입니다
        var imageSize = new kakao.maps.Size(24, 35); 
        
        // 마커 이미지를 생성합니다    
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
        
        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            map: map, // 마커를 표시할 지도
            position: this.positions[i].latlng, // 마커를 표시할 위치
            title : this.positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            image : markerImage // 마커 이미지 
        })
      }
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