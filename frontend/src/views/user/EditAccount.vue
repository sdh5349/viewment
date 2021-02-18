<template>
  <v-row justify="center">
    <v-col lg="4" md="4" sm="6">
      <Alert
        v-if="alert.alerted"
        :message="alert.message"
        :color="alert.color ? alert.color : 'error'"
      />
      <!-- 계정 설정 버튼 시작 -->
      <v-btn class="my-1 justify-start v-btn-text" text x-large block @click="onNotiSettingsButton">
        <v-icon class="mr-4" left large>
          mdi-bell
        </v-icon>
        알람 설정
      </v-btn>
      <v-btn
        class="my-1 justify-start v-btn-text"
        text
        x-large
        block
        @click="onChangePasswordButton"
      >
        <v-icon class="mr-4" left large>
          mdi-account-edit
        </v-icon>
        비밀번호 변경
      </v-btn>
      <v-btn class="my-1 justify-start v-btn-text" text x-large block @click="onLogoutButton">
        <v-icon class="mr-4" left large>
          mdi-logout
        </v-icon>
        로그아웃
      </v-btn>
      <v-btn class="my-1 justify-start v-btn-text" text x-large block @click="onAccountDelete">
        <v-icon class="mr-4" left large>
          mdi-delete
        </v-icon>
        회원탈퇴
      </v-btn>
      <Confirm
        v-if="confirm.confirmed"
        :message="confirm.message"
        @on-confirmed="onDeleteConfirmed"
      />
      <!-- 계정 설정 버튼 끝 -->
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios';
import firebase from 'firebase/app';
import Alert from '@/components/common/Alert';
import Confirm from '@/components/common/Confirm';

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: 'EditAccount',
  components: {
    Alert,
    Confirm,
  },
  data() {
    return {
      alert: {
        alerted: false,
        message: '',
        color: '',
      },
      confirm: {
        confirmed: false,
        message: '',
      },
      profileUserId: sessionStorage.getItem('uid'),
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
  methods: {
    onNotiSettingsButton() {
      this.$router.push({ name: 'NotiSettings' });
    },
    onChangePasswordButton() {
      this.$router.push({ name: 'ChangePassword' });
    },
    onLogoutButton() {
      const self = this;

      firebase
        .auth()
        .signOut()
        .then(() => {
          sessionStorage.removeItem('jwt');
          sessionStorage.removeItem('uid');
          self.$emit('logout');
          self.$router.push({ name: 'Login' });
        })
        .catch((err) => {
          this.alert.message = err.message;
          this.alert.alerted = true;
        });
    },
    onAccountDelete() {
      this.confirm.confirmed = true;
      this.confirm.message = '정말 회원탈퇴 하시겠습니까?';
    },
    onDeleteConfirmed(confirmed) {
      if (confirmed) {
        // 서버 DB에서 삭제
        axios
          .delete(`${SERVER_URL}/accounts/${this.profileUserId}`, this.getToken)
          .then(() => {
            var user = firebase.auth().currentUser;
            const self = this;

            // firebase DB에서 삭제
            user
              .delete()
              .then(() => {
                self.alert.message = '그동안 이용해주셔서 감사합니다.!';
                self.alert.color = 'primary';
                self.alert.alerted = true;
                self.confirm.confirmed = false;
                setTimeout(() => self.onLogoutButton(), 2000);
              })
              .catch((err) => {
                self.alert.message = err.message;
                self.alert.alerted = true;
                self.confirm.confirmed = false;
              });
          })
          .catch((err) => {
            this.alert.message = err.message;
            this.alert.alerted = true;
            this.confirm.confirmed = false;
          });
      } else {
        this.confirm.confirmed = false;
      }
    },
  },
};
</script>

<style scoped>
/* 버튼 내에 들어갈 텍스트의 크기를 조절한다 */
.v-btn-text {
  font-size: 1.2rem;
}
</style>
