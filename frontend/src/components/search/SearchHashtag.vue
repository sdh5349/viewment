<template>
  <v-row
  justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
      <v-card
        class="mx-auto mt-5"
        flat
      >
        <v-list>
          <v-list-item
            v-for="hash in keyword.slice(0,9)"
            :key="hash.content"
          >
            <v-list-item-avatar>
              <v-icon>
                mdi-pound
              </v-icon>
            </v-list-item-avatar>

            <v-list-item-content>
              <v-list-item-title v-text="hash.content"></v-list-item-title>
            </v-list-item-content>

          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return {
      keyword: [],
    }
  },
  props: {
    search : {
      type: String,
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
  methods: {
    getHashtags(keyword) {
      console.log(this.getToken)
      axios.get(`http://i4b105.p.ssafy.io:8080/api/v1/hashtags/${keyword}`, this.getToken)
        .then((res) => {
        console.log(res)        
        })
        .catch((err)=> {
          alert('error'+err.message)
        })
    },
  },
  watch: {
    search: function(res) {
      this.getHashtags(res)
    }
  },
}
</script>

<style>

</style>