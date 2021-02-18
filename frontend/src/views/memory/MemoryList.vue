<template>
  <v-row
    v-if="loading"
    style="height: 50vh;"
    class="fill-height ma-0"
    align="center"
    justify="center"
  >
    <v-progress-circular
      indeterminate
      color="primary"
    ></v-progress-circular>
  </v-row>
  <v-row
    v-else
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
      <v-virtual-scroll
        :items="memories"
        :item-height="50"
        max-height="80vh"
        @scroll.native="scrolling"
      >
        <template v-slot:default="{ item }">
          <v-list-item
            @click="onMemoryListItem(item)"
          >
            <v-list-item-content>
              <v-list-item-title>
                <div class="d-flex justify-space-between">
                  <div>
                    <!-- 아이콘 or 프로필 썸네일, 사용자 닉네임 시작 -->
                    <v-icon
                      class="mr-4" 
                      left 
                      large
                    > 
                      mdi-map-marker 
                    </v-icon>
                      {{ item.name }}
                    <!-- 아이콘 or 프로필 썸네일, 사용자 닉네임 끝 -->
                  </div>

                  <!-- 메모리 삭제(본인 기억하기 리스트일 경우) 버튼 시작 -->
                  <v-btn 
                    v-if="profileUserId === loginUserId"
                    small 
                    class="align-self-center px-0"
                    width="55"
                    color="error" 
                    elevation="1" 
                    @click.stop="onMemoryDeleteButton(item)"
                  >
                    삭제
                  </v-btn>
                  <!-- 메모리 삭제(본인 기억하기 리스트일 경우) 버튼 끝 -->

                </div>
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-virtual-scroll>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'MemoryList',
  props: {
    profileUserId: String,
  },
  data() {
    return {
      loading: true,
      loginUserId: '',
      memories: [],
    }
  },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt')

      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 현재 프로필 사용자의 메모리 정보를 원하는 갯수 만큼 요청하는 메서드
    fetchData() {
      // 현재 사용자 구하기
      // 필요한 데이터 가져오기
      axios.get(`${SERVER_URL}/users/${this.profileUserId}/memories`, this.getToken)
      .then(res => {
        this.memories = res.data
      })
      .then(res => {
        this.loginUserId = sessionStorage.getItem('uid')
        this.loading = false
      })
      .catch(err => {
        alert("오류"); // TODO: 오류페이지로 변경
        console.log('Error', err.message);
        // self.$router.push({ name: 'Error' })
      })
    },
    onMemoryListItem (targetMemory) {
      this.$router.push({ 
        name: 'NewsFeed', 
        params: { lat: targetMemory.lat, lng: targetMemory.lng }
      })
    },
    // 본인 팔로워 리스트일 경우 삭제를 희망하는 유저의 인덱스를 찾아 삭제하는 메서드
    onMemoryDeleteButton (targetMemory) {
      if (confirm("삭제하시겠습니까?")) {
        axios.delete(`${SERVER_URL}/memories/${targetMemory.memoryId}`, this.getToken)
        .then(() => {
        const targetMemoryIdx = this.memories.indexOf(targetMemory)
        this.memories.splice(targetMemoryIdx, 1)
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      }
    },
  }
}
</script>

<style scoped>
/* 스크롤 컨테이너 안의 아이템이 넘쳐도 스크롤 컨테이너의 크기는 고정 */
  .scroll-container {
    width: 100%;
    height: 100%;
    margin-bottom: 50px;
  }
</style>