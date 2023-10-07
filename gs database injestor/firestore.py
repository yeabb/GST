import openpyxl
import firebase_admin
from firebase_admin import credentials, firestore

# Initialize Firebase Admin SDK with your google-service.json file
cred = credentials.Certificate("credentials.json")
firebase_admin.initialize_app(cred)

# Load the Excel file
excel_file = openpyxl.load_workbook('gas.xlsx')
sheet = excel_file.active

# Get a reference to the Firestore database
db = firestore.client()

# Create a set to store unique GeoPoints
unique_geopoints = set()

# Iterate through the rows of the sheet
for row in sheet.iter_rows(min_row=2, values_only=True):  # Assuming headers are in the first row
    garageName, garageAddress, garagePhone, garageLatitude, garageLongitude = row[1], row[4], row[9], row[15], row[16]

    # Create a GeoPoint object
    geo_point = firestore.GeoPoint(float(garageLatitude), float(garageLongitude))

    # Check if the GeoPoint is already in the set
    geo_point_str = f"{geo_point.latitude},{geo_point.longitude}"
    if geo_point_str in unique_geopoints:
        print(f"Skipping {garageName} with duplicate location.")
        continue

    # Add the GeoPoint to the set
    unique_geopoints.add(geo_point_str)

    # Create a dictionary to represent the GasStation object
    garage = {
        "garageName": str(garageName),
        "garageAddress": str(garageAddress),
        "garagePhone": str(garagePhone),
        "location": geo_point  # Use GeoPoint field for location

    }

    # Add the data to Firestore
    try:
        doc_ref, doc_id = db.collection('garagesss').add(garage)  # Access the document ID from the tuple
        print(f"Successfully added data for {garageName} with document ID: {doc_id}")
    except Exception as e:
        print(f"Failed to add data for {garageName}: {str(e)}")

# Close the Excel file when done
excel_file.close()







