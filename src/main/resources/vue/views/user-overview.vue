<template id="user-overview">
  <app-layout>
    <!-- Card - for adding a new user -->
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Users
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addUser">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-userId">User Id</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userId" name="UserId" placeholder="UserId"/>
          </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="input-user-firstName">First Name</span>
              </div>
              <input type="text" class="form-control" v-model="formData.firstName" name="firstName" placeholder="First Name"/>
            </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-last-name">Last Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.lastName" name="lastName" placeholder="Last Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-gender">Gender</span>
            </div>
            <input type="text" class="form-control" v-model="formData.gender" name="gender" placeholder="Gender"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="text" class="form-control" v-model="formData.email" name="email" placeholder="Email"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-mobile">Mobile</span>
            </div>
            <input type="text" class="form-control" v-model="formData.mobile" name="mobile" placeholder="Mobile"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-age">Age</span>
            </div>
            <input type="text" class="form-control" v-model="formData.age" name="age" placeholder="Age"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-address">Address</span>
            </div>
            <input type="text" class="form-control" v-model="formData.address" name="address" placeholder="Address"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-height">Height</span>
            </div>
            <input type="text" class="form-control" v-model="formData.height" name="height" placeholder="Height"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-weight">Weight</span>
            </div>
            <input type="text" class="form-control" v-model="formData.weight" name="weight" placeholder="Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-userName">User Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.userName" name="userName" placeholder="User Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-password">Password</span>
            </div>
            <input type="text" class="form-control" v-model="formData.password" name="password" placeholder="Password"/>
          </div>
          </form>
          <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addUser()">Add User</button>
        </div>
    </div>

    <!-- List Group - displays all the users -->
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(user,index) in users" v-bind:key="index">
        <div class="mr-auto p-2">
          <span><a :href="`/users/${user.userId}`"> {{ user.firstName }} {{ user.lastName }} {{ user.gender }} ({{ user.email }}) {{ user.mobile }} {{ user.age }} {{ user.address }} {{ user.height }} {{ user.weight }} {{ user.userName }} {{ user.password }}</a></span>
        </div>
        <div class="p2">
          <a :href="`/users/${user.userId}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
             @click="deleteUser(user, index)">
             <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("user-overview", {
  template: "#user-overview",
  data: () => ({
    users: [],
    formData: [],
    hideForm :true,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    deleteUser: function (user, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const userId = user.id;
        const url = `/api/users/${userId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.users.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addUser: function (){
      const url = `/api/users`;
      axios.post(url,
          {
            userId: this.formData.userId,
            firstName: this.formData.firstName,
            lastName: this.formData.lastName,
            gender: this.formData.gender,
            email: this.formData.email,
            mobile: this.formData.mobile,
            age: this.formData.age,
            address: this.formData.address,
            height: this.formData.height,
            weight: this.formData.weight,
            userName: this.formData.userName,
            password: this.formData.password
          })
          .then(response => {
            this.users.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>