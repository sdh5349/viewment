<template>
  <div>
    <v-container class="py-0">
      <v-row >
            <v-text-field 
              v-model="address" 
              label='주소 검색'
              class="address-search-field"
              height="50px"
              >
            </v-text-field>

            <v-card class="address-list-card">
            <v-list
              
              >
              <v-list-item
                
                
                v-for="(searchedLocation, i) in searchedLocations.slice(0,9)"
                :key="i"
                @click="searchAddress(searchedLocation)"
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

        
  
        <v-col cols='12' class="px-0">
        
        <div class="map_wrap">
          <div id="map" class="map" ></div>
          <div class="hAddr">
            <span id="centerAddr">{{addressName}}</span>
          </div>
        </div>
        </v-col>
    
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL
export default {
  components: {
    
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
      searchedLocations: '',
      markers: [],
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
      self.address = ''
      self.searchedLocations = []
      
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
    getLocation(res) {
      const self = this
      console.log(res)
      var places = new kakao.maps.services.Places()

      var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
          self.searchedLocations = result
        }
      };
      places.keywordSearch(res, callback);
    },
    articleMarkers() {   
      const self = this
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
          self.markerInfo.setMap(null)
          axios.get(`${SERVER_URL}/pins/${id}`, self.getToken)
          .then((res)=> {
            self.$emit('onClick', res.data)
            alert('이 마커로 위치 지정 완료')
          }) 
        }
      }
      var clusterer = new kakao.maps.MarkerClusterer({
      map: self.map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
      averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
      minLevel: 8 // 클러스터 할 최소 지도 레벨 
      })
      clusterer.addMarkers(self.markers)
    },
    getArticle() {
      
      const self = this
      axios.get(`${SERVER_URL}/pins`, this.getToken)
      .then((res)=> {
        
        this.articles = res.data
        this.articleMarkers()
      }) 
    },    
  },
  watch: {
    address: function(res) {
      this.getLocation(res)
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
  created() {
    this.getArticle()
  },
}
</script>

<style scoped>
.address-search-field {
  position: absolute;
}
.address-list-card {
  position: absolute;
  top: 50px;
  z-index: 9999;
  height: 600px;
}
.map_wrap {
  position:relative;
  width:100%;
  height:450px;
  top: 100px;
}

.map {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 0;
}
.hAddr {
  position:absolute;
  left:0px;
  border-radius: 2px;
  background:#fff;
  background:rgba(255,255,255,0.8);
  z-index:1;
  padding:5px;
  z-index: 9000;
}
#centerAddr {
  display:block;
  margin-top:2px;
  font-weight: normal;
  font-size: 10%
}

</style>