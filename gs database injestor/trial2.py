import json
import pyrebase
import google.cloud.storage

with open("credentials.json", "r") as f:
  config = json.load(f)

firebase = pyrebase.FirebaseApplication(config)

storage = google.cloud.storage.Client()
bucket = storage.get_bucket(config["gs://gas-station-tracker-f16a4.appspot.com"])

blob = bucket.blob("1.jpeg")
blob.upload_from_filename("1.jpeg")

download_url = blob.public_url

db = firebase.database()
db.child("images").push({
  "url": download_url
})