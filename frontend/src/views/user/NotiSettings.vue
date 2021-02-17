<template>
  <v-row justify="center">
    <v-col lg="4" md="4" sm="6">
      <Alert
        v-if="alert.alerted"
        :message="alert.message"
        :color="alert.color ? alert.color : 'error'"
      />
      <v-switch v-model="memoryNoti" label="기억하기" @click="changeNoti('memory')"></v-switch>
      <v-switch v-model="followNoti" label="팔로우" @click="changeNoti('follow')"></v-switch>
      <v-switch v-model="likeNoti" label="좋아요" @click="changeNoti('like')"></v-switch>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios';
import firebase from 'firebase/app';
import Alert from '@/components/common/Alert';

const SERVER_URL = process.env.VUE_APP_SERVER_URL;
const VAPID_KEY = process.env.VUE_APP_VAPID_KEY;

export default {
  name: 'NotiSettings',
  components: {
    Alert,
  },
  data: () => ({
    alert: {
      alerted: false,
      message: '',
      color: '',
    },
    profileUserId: sessionStorage.getItem('uid'),
    memoryNoti: false,
    followNoti: false,
    likeNoti: false,
    regiToken: '',
  }),
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
      axios
        .get(`${SERVER_URL}/notis/${this.profileUserId}`, this.getToken)
        .then((res) => {
          this.memoryNoti = res.data.memoryNoti;
          this.followNoti = res.data.followNoti;
          this.likeNoti = res.data.likeNoti;
        })
        .catch((err) => {
          this.alert.message = err.message;
          this.alert.alerted = true;
        });
      firebase
        .messaging()
        .getToken({
          vapidKey: `${VAPID_KEY}`,
        })
        .then((currentToken) => {
          this.regiToken = currentToken;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    changeNoti(target) {
      if (!this.getStatus(target)) {
        axios
          .put(
            `${SERVER_URL}/notis/off`,
            {
              regiToken: this.regiToken,
              type: target,
              userId: this.profileUserId,
            },
            this.getToken
          )
          .then(() => this.changeStatus(target))
          .catch((err) => {
            this.alert.message = err.message;
            this.alert.alerted = true;
          });
      } else {
        axios
          .put(
            `${SERVER_URL}/notis/on`,
            {
              regiToken: this.regiToken,
              type: target,
              userId: this.profileUserId,
            },
            this.getToken
          )
          .then(() => this.changeStatus(target))
          .catch((err) => {
            this.alert.message = err.message;
            this.alert.alerted = true;
          });
      }
    },
    getStatus(target) {
      switch (target) {
        case 'memory':
          return this.memoryNoti;
        case 'follow':
          return this.followNoti;
        case 'like':
          return this.likeNoti;
      }
    },
    changeStatus(target) {
      switch (target) {
        case 'memory':
          this.memoryNoti != this.memoryNoti;
          break;
        case 'follow':
          this.followNoti != this.followNoti;
          break;
        case 'like':
          this.likeNoti != this.likeNoti;
          break;
      }
    },
  },
};
</script>

<style></style>
