<template>
  <v-row
    v-if="loading"
    style="height: 50vh;"
    class="fill-height ma-0"
    align="center"
    justify="center"
  >
    <v-progress-circular indeterminate color="primary"></v-progress-circular>
  </v-row>
  <v-row v-else-if="noData" justify="center">
    <v-alert v-html="'알림이 없습니다'" class="ma-0 pa-2 text-center" type="info"> </v-alert>
  </v-row>
  <v-row v-else justify="center">
    <v-col lg="4" md="4" sm="6">
      <v-virtual-scroll :items="list" :item-height="90" max-height="200vh">
        <template v-slot:default="{ item }">
          <v-list-item @click="onNotiListItem(item)">
            <v-list-item-content>
              <div class="d-flex">
                <div>
                  <v-avatar size="3rem">
                    <img v-if="item.type == 'MEMORY'" src="@/assets/images/flag2.png" />
                    <img
                      v-else-if="item.type != 'MEMORY' && item.from.profileImage"
                      :src="prefix + item.from.profileImage.path"
                    />
                    <img v-else src="@/assets/images/account.png" />
                  </v-avatar>
                </div>
                <div style="margin: 0; padding: 2px 13px">
                  <p v-if="item.type == 'MEMORY'" style="line-height: 1.5">
                    회원님의 기억장소 <strong> {{ item.memoryName }}</strong> 에 새로운 글이
                    올라왔습니다
                  </p>
                  <p v-else-if="item.type == 'FOLLOW'" style="line-height: 1.5">
                    <strong> {{ item.from.nickname }}</strong> 님이 회원님을 팔로우하기 시작했습니다
                  </p>
                  <p v-else style="line-height: 1.5">
                    <strong> {{ item.from.nickname }}</strong> 님이 회원님의 게시글을 좋아합니다
                  </p>
                </div>
              </div>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-virtual-scroll>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios';
import firebase from 'firebase/app';

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: 'NotiList',
  components: {},
  data() {
    return {
      alert: {
        alerted: false,
        message: '',
        color: '',
      },
      noData: false,
      loading: true,
      list: [],
      prefix: `${SERVER_URL}/images/`,
    };
  },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt');

      const config = {
        headers: {
          'X-Authorization-Firebase': token,
        },
      };
      return config;
    },
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      const self = this;
      firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
          user
            .getIdToken(/* forceRefresh */ true)
            .then(function(idToken) {
              axios
                .get(
                  'https://pjt-auth-97ed8-default-rtdb.firebaseio.com/noti/' +
                    sessionStorage.getItem('uid') +
                    '.json?auth=' +
                    idToken
                )
                .then((res) => {
                  console.log(res.data);
                  if (res.data != null) {
                    const keys = Object.keys(res.data);
                    for (let index = keys.length - 1; index >= 0; index--) {
                      self.list.push(res.data[keys[index]]);
                    }
                  } else {
                    self.noData = true;
                  }
                  console.log(self.list);
                  self.loading = false;
                })
                .catch((err) => {
                  console.log(err);
                });
            })
            .catch(function(error) {
              console.log(error);
            });
        }
      });
      // firebase
      //   .auth()
      //   .currentUser.getIdToken(/* forceRefresh */ true)
      //   .then(function(idToken) {
      //     axios
      //       .get(
      //         'https://pjt-auth-97ed8-default-rtdb.firebaseio.com/noti/' +
      //           sessionStorage.getItem('uid') +
      //           '.json?auth=' +
      //           idToken
      //       )
      //       .then((res) => {
      //         console.log(res.data);
      //         if (res.data != null) {
      //           const keys = Object.keys(res.data);
      //           for (let index = keys.length - 1; index >= 0; index--) {
      //             self.list.push(res.data[keys[index]]);
      //           }
      //         } else {
      //           self.noData = true;
      //         }
      //         console.log(self.list);
      //         self.loading = false;
      //       })
      //       .catch((err) => {
      //         console.log(err);
      //       });
      //   })
      //   .catch(function(error) {
      //     console.log(error);
      //   });
      console.log(this.list);
    },
    onNotiListItem(item) {
      if (item.type == 'FOLLOW') {
        this.$router
          .push({
            name: 'Profile',
            params: { profileUserId: item.from.userId },
          })
          .catch((err) => {
            if (err.name === 'NavigationDuplicated') {
              location.reload();
            }
          });
      } else {
        this.$router.push({
          name: 'DetailArticle',
          params: { articleId: item.articleId },
        });
      }
    },
  },
};
</script>

<style></style>
