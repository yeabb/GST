import os
import firebase_admin
from firebase_admin import credentials, firestore, storage
import openpyxl

# Initialize Firebase Admin SDK with your credentials.json file and storageBucket option
cred = credentials.Certificate("credentials.json")
firebase_admin.initialize_app(cred, {
    'storageBucket': 'gs://gas-station-tracker-f16a4.appspot.com'
})

# Initialize Firestore
db = firestore.client()

# Initialize Firebase Storage
bucket = storage.bucket()

# Load the Excel file
excel_file = openpyxl.load_workbook('carsRent.xlsx')
sheet = excel_file.active

# Iterate through the rows of the sheet
for row in sheet.iter_rows(min_row=2, values_only=True):  # Assuming headers are in the first row
    carOwnerFirstName, carOwnerLastName, carOwnerPhone, carPlateNumber, carMake, carModelName, carModelYear, carStatus= row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]

    # Create a dictionary to represent the car object
    car = {
        "carOwnerFirstName": str(carOwnerFirstName),
        "carOwnerLastName": str(carOwnerLastName),
        "carOwnerPhone": str(carOwnerPhone),
        "carPlateNumber": str(carPlateNumber),
        "carMake": str(carMake),
        "carModelName": str(carModelName),
        "carModelYear": str(int(carModelYear)),
        "carStatus": str(carStatus),
        "imageURLs": []  # Initialize an empty list for image URLs
    }

    # Upload the images to Firebase Storage and get their download URLs
    for image_index in range(1, 6):  # Assuming there are 5 images
        image_filename = f'{image_index}.jpeg'
        
        image_blob = bucket.blob(f'images/{carPlateNumber}_{image_index}.jpeg')
        image_blob.upload_from_filename(image_filename)
        image_url = image_blob.public_url
        car["imageURLs"].append(image_url)

    # Add the data to Firestore
    try:
        doc_ref = db.collection('carsTrial').add(car)
        print(f"Successfully added data for {carPlateNumber} with document ID: {doc_ref.id}")
    except Exception as e:
        print(f"Failed to add data for {carPlateNumber}: {str(e)}")

# Close the Excel file when done
excel_file.close()
