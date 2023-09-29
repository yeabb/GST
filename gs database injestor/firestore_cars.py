import openpyxl
import firebase_admin
from firebase_admin import credentials, firestore

# Initialize Firebase Admin SDK with your google-service.json file
cred = credentials.Certificate("credentials.json")
firebase_admin.initialize_app(cred)

# Load the Excel file
excel_file = openpyxl.load_workbook('cars.xlsx')
sheet = excel_file.active

# Get a reference to the Firestore database
db = firestore.client()

# Create a set to store unique GeoPoints
unique_geopoints = set()

# Iterate through the rows of the sheet
for row in sheet.iter_rows(min_row=2, values_only=True):  # Assuming headers are in the first row
    carSellerFirstName, carSellerLastName, carSellerPhone, carPlateNumber, carMake, carModelName, carModelYear, carStatus= row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]

    

    # Create a dictionary to represent the car object
    car = {
        "carSellerFirstName": str(carSellerFirstName),
        "carSellerLastName": str(carSellerLastName),
        "carSellerPhone": str(carSellerPhone),
        "carPlateNumber": str(carPlateNumber),
        "carMake": str(carMake),
        "carModelName": str(carModelName),
        "carModelYear": str(carModelYear),
        "carStatus": str(carStatus)

    }

    # Add the data to Firestore
    try:
        doc_ref, doc_id = db.collection('cars').add(car)  # Access the document ID from the tuple
        print(f"Successfully added data for {carPlateNumber} with document ID: {doc_id}")
    except Exception as e:
        print(f"Failed to add data for {carPlateNumber}: {str(e)}")

# Close the Excel file when done
excel_file.close()






