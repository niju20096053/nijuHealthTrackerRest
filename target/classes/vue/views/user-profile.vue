<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.userId" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-first-name">First Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.firstName" name="firstName" placeholder="First Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text" id="input-user-last-name">Last Name</span>
            </div>
              <input type="text" class="form-control" v-model="user.lastName" name="lastName" placeholder="Last Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-gender">Gender</span>
            </div>
            <input type="text" class="form-control" v-model="user.gender" name="gender" placeholder="Gender"/>
          </div>
          <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-user-email">Email</span>
                </div>
                <input type="text" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
          <div class="input-group mb-3">
                  <div class="input-group-prepend">
                    <span class="input-group-text" id="input-user-mobile">Mobile</span>
                  </div>
                  <input type="text" class="form-control" v-model="user.mobile" name="mobile" placeholder="Mobile"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-age">Age</span>
            </div>
            <input type="text" class="form-control" v-model="user.age" name="age" placeholder="Age"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-address">Address</span>
            </div>
            <input type="text" class="form-control" v-model="user.address" name="address" placeholder="Address"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-height">Height</span>
            </div>
            <input type="text" class="form-control" v-model="user.height" name="height" placeholder="Height"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-weight">Weight</span>
            </div>
            <input type="text" class="form-control" v-model="user.weight" name="weight" placeholder="Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-userName">User Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.userName" name="userName" placeholder="User Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-password">Password</span>
            </div>
            <input type="text" class="form-control" v-model="user.password" name="password" placeholder="Password"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <p  v-if="activities.length == 0"> No activities yet...</p>
        <p  v-if="activities.length > 0"> Activities so far...</p>
        <ul>
          <li v-for="activity in activities">
            {{ activity.description }} for {{ activity.duration }} minutes
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    meals: []
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
    axios.get(url + `/meals`)
        .then(res => this.meals = res.data)
        .catch(error => {
          console.log("No meals added yet (this is ok): " + error)
        })
  },
  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`
      axios.patch(url,
          {
            firstName: this.formData.firstName,
            lastName: this.formData.lastName,
            gender: this.formData.gender,
            email: this.formData.email,
            mobile: this.formDate.mobile,
            age: this.formData.age,
            address: this.formData.address,
            height: this.formData.height,
            weight: this.formData.weight,
            userName: this.formData.userName,
            password: this.formData.password
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User updated!")
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`
        axios.delete(url)
            .then(response => {
              alert("User deleted")
              //display the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>
