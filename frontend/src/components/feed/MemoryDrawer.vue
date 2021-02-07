<template>
  <div>
    <v-container class="py-0">
      <v-row>
        <v-col>
          <v-sheet
            fixed
            class="mb-5"
            height="100"
          >
            <v-btn
              color="pink"
              dark
              @click.stop="drawer = !drawer"
              block
            >^</v-btn>
        
            <v-navigation-drawer
              v-model="drawer"
              bottom
              class="memoryDrawer"
              height="400px"
              min-height="400px"
              hide-overlay
            >
              <v-list dense>
                <v-list-item
                  v-for="memory in memories"
                  :key="memory.memoryId"
                >
                  <v-list-item-icon>
                    <v-icon>
                      mdi-map-marker
                    </v-icon>
                  </v-list-item-icon>

                  
                  <v-list-item-content>
                    <v-btn @click='goMemory(memory)'>
                      <v-list-item-title>{{ memory.name }}</v-list-item-title>
                    </v-btn>
                  </v-list-item-content>
                  
                </v-list-item>
              </v-list>
            </v-navigation-drawer>
          </v-sheet>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
const SERVER_URL = process.env.VUE_APP_SERVER_URL
import axios from 'axios'

export default {
  data() {
    return {
      drawer: null,
      memories: '',
    }
  },
  methods: {
    getMemories() {
      const userId = sessionStorage.getItem('uid')
      axios.get(`${SERVER_URL}/users/${userId}/memories`, this.getToken)
      .then((res) => {
        this.memories = res.data
        this.$emit('getMemories', this.memories)
      })
    },
    goMemory(res) {
      this.$emit('goToMemory' , res)
      this.drawer = !this.drawer
    }
  },
  created() {
    this.getMemories()
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
    }
  },
}
</script>

<style scoped>
.memoryDrawer{
  z-index: 10;
  position: relative;
}
</style>