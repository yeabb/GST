import openpyxl
import firebase_admin
from firebase_admin import credentials, firestore

# Initialize Firebase Admin SDK with your google-service.json file
cred = credentials.Certificate("credentials.json")
firebase_admin.initialize_app(cred)

# Load the Excel file
excel_file = openpyxl.load_workbook('carsRent.xlsx')
sheet = excel_file.active

# Get a reference to the Firestore database
db = firestore.client()

# Create a set to store unique GeoPoints
unique_geopoints = set()

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
        "carImageUrls": ["https://firebasestorage.googleapis.com/v0/b/gas-station-tracker-f16a4.appspot.com/o/1.jpeg?alt=media&token=4ad5cd61-ab8b-4e22-957f-1b62600fc668&_gl=1*48thgd*_ga*NjcyMzU3NzE5LjE2OTUwNzU0NzE.*_ga_CW55HF8NVT*MTY5NjYxOTg2Mi41MC4xLjE2OTY2MjAxNDUuMzYuMC4w", 
                         "https://firebasestorage.googleapis.com/v0/b/gas-station-tracker-f16a4.appspot.com/o/2.jpeg?alt=media&token=e322034b-4b6b-43df-9082-c914ebb8f9ae&_gl=1*1tesrbr*_ga*NjcyMzU3NzE5LjE2OTUwNzU0NzE.*_ga_CW55HF8NVT*MTY5NjYxOTg2Mi41MC4xLjE2OTY2MjA1NTAuMTcuMC4w",
                         "https://firebasestorage.googleapis.com/v0/b/gas-station-tracker-f16a4.appspot.com/o/3.jpeg?alt=media&token=9962cdc9-959b-462e-86d8-6dbc28ebae4f&_gl=1*xiymet*_ga*NjcyMzU3NzE5LjE2OTUwNzU0NzE.*_ga_CW55HF8NVT*MTY5NjYxOTg2Mi41MC4xLjE2OTY2MjA1OTYuNjAuMC4w",
                         "https://firebasestorage.googleapis.com/v0/b/gas-station-tracker-f16a4.appspot.com/o/4.jpeg?alt=media&token=975e20ec-ed01-4d80-9c61-4692da7bdac1&_gl=1*252ur6*_ga*NjcyMzU3NzE5LjE2OTUwNzU0NzE.*_ga_CW55HF8NVT*MTY5NjYxOTg2Mi41MC4xLjE2OTY2MjA2MTIuNDQuMC4w",
                         "https://firebasestorage.googleapis.com/v0/b/gas-station-tracker-f16a4.appspot.com/o/5.jpeg?alt=media&token=6c51a5f9-3cf4-410d-8811-477e6eaef56a&_gl=1*1lutqge*_ga*NjcyMzU3NzE5LjE2OTUwNzU0NzE.*_ga_CW55HF8NVT*MTY5NjYxOTg2Mi41MC4xLjE2OTY2MjA2MjQuMzIuMC4w"]
        

    }
    

    # Add the data to Firestore
    try:
        doc_ref, doc_id = db.collection('carsForRent').add(car)  # Access the document ID from the tuple
        print(f"Successfully added data for {carPlateNumber} with document ID: {doc_id}")
    except Exception as e:
        print(f"Failed to add data for {carPlateNumber}: {str(e)}")

# Close the Excel file when done
excel_file.close()
