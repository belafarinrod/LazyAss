workflow "Build, Test " {
  on = "push"
  resolves = ["Build", "Run UI Tests "]
  
}

action "Build" {
  uses = "vgaidarji/android-github-actions/build@v1.0.0"
  secrets = ["FABRIC_API_KEY", "FABRIC_API_SECRET"]
  args = "./gradlew assembleDebug -PpreDexEnable=false"
}
action


action "Run UI Tests" {
  needs = ["Build"]
  secrets = ["FABRIC_API_KEY", "FABRIC_API_SECRET"]
  uses = "vgaidarji/android-github-actions/emulator@v1.0.0"
}