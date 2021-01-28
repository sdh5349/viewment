<template>
<v-container>
  
  <v-row
  justify="center"
  >
    <v-col 
      class="ml-5"
    >
      <v-list 
        lg="4"
        md="4"
        sm="6"
        subheader
      >
        <v-subheader>Recent chat</v-subheader>
        <v-list-item
          v-for="chat in recent"
          :key="chat.title"
        >
          <v-list-item-avatar>
            <v-img
              :alt="`${chat.title} avatar`"
              :src="chat.avatar"
            ></v-img>
          </v-list-item-avatar>
  
          <v-list-item-content>
            <v-list-item-title v-text="chat.title"></v-list-item-title>
          </v-list-item-content>
  
          <v-list-item-icon
            justifty=""
          >
            <v-icon :color="chat.active ? 'deep-purple accent-4' : 'grey'">
              mdi-close
            </v-icon>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
    </v-col>
  </v-row>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    recent: [
      {
        active: true,
        avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
        title: 'Jason Oner',
      },
      {
        active: true,
        avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
        title: 'Mike Carlson',
      },
      {
        avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
        title: 'Cindy Baker',
      },
      {
        avatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
        title: 'Ali Connors',
      },
    ],
  }),
  props: {
    select : {
      type: Object
    }
  },
}

</script>

<style>

</style>