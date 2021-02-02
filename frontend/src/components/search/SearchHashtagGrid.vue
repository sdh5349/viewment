<template>
    <v-row 
      justify="center"
    >
      <v-col
        lg="4"
        md="4"
        sm="6"
      >
        <v-card>
          <v-img
            :src="hashtag.img"
            class="white--text align-end"
            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
            height="100px"
            width="100px"
          >
            <!-- <v-card-title v-text="hashtag.hid"></v-card-title> -->
          </v-img>

          <!-- <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn icon>
              <v-icon>mdi-heart</v-icon>
            </v-btn>

            <v-btn icon>
              <v-icon>mdi-bookmark</v-icon>
            </v-btn>

            <v-btn icon>
              <v-icon>mdi-share-variant</v-icon>
            </v-btn>
          </v-card-actions> -->
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
      hash: "",
    }
  },
  props: {
    // search : {
    //   type: String,
    // }
  },
  methods: {
    // getHash() {
    //   axios.get(`${SERVER_URL}`)
    // }
  }
}
</script>

<style>

</style>