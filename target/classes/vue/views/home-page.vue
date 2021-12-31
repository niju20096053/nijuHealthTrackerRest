<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Users Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Activities Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Meals</h5>
          <div class="card-body">
            <h5 class="card-title">{{meals.length}} meals</h5>
            <a href="/meals" class="btn btn-primary">More Meals Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Sleep</h5>
          <div class="card-body">
            <h5 class="card-title">{{sleeps.length}} sleeps</h5>
            <a href="/meals" class="btn btn-primary">More Sleeps Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Yogas</h5>
          <div class="card-body">
            <h5 class="card-title">{{yogas.length}} yogas</h5>
            <a href="/meals" class="btn btn-primary">More Yogas Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Waters</h5>
          <div class="card-body">
            <h5 class="card-title">{{waters.length}} waters</h5>
            <a href="/meals" class="btn btn-primary">More Waters Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Goals</h5>
          <div class="card-body">
            <h5 class="card-title">{{goals.length}} goals</h5>
            <a href="/meals" class="btn btn-primary">More Goals Details...</a>
          </div>
        </div>
      </div>
    </div>
    <div class="col">
      <div class="card">
        <h5 class="card-header">Total Health Conditions</h5>
        <div class="card-body">
          <h5 class="card-title">{{healths.length}} health conditions</h5>
          <a href="/meals" class="btn btn-primary">More Health Conditions Details...</a>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        meals: [],
        sleeps:[],
        yogas: [],
        waters: [],
        goals: [],
        healths: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/meals")
            .then(res => this.meals = res.data)
            .catch(() => alert("Error while fetching meals"));
        axios.get("/api/sleeps")
            .then(res => this.sleeps = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/yogas")
            .then(res => this.yogas = res.data)
            .catch(() => alert("Error while fetching yogas"));
        axios.get("/api/waters")
            .then(res => this.waters = res.data)
            .catch(() => alert("Error while fetching water"));
        axios.get("/api/goals")
            .then(res => this.goals = res.data)
            .catch(() => alert("Error while fetching goals"));
        axios.get("/api/healths")
            .then(res => this.healths = res.data)
            .catch(() => alert("Error while fetching medical conditions"));
      }
    });
</script>


<style>
.card{
  background-color: rgba(167, 237, 238, 0.99);
}
.btn-primary{
  background-color: #70f6af;
  color: black;
}
.col{
  padding: 10px;
}
</style>
