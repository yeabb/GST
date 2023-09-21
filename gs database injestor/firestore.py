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
    gasStationName, gasStationAddress, gasStationPhone, gasStationLatitude, gasStationLongitude = row[1], row[4], row[9], row[15], row[16]

    # Create a GeoPoint object
    geo_point = firestore.GeoPoint(float(gasStationLatitude), float(gasStationLongitude))

    # Check if the GeoPoint is already in the set
    geo_point_str = f"{geo_point.latitude},{geo_point.longitude}"
    if geo_point_str in unique_geopoints:
        print(f"Skipping {gasStationName} with duplicate location.")
        continue

    # Add the GeoPoint to the set
    unique_geopoints.add(geo_point_str)

    # Create a dictionary to represent the GasStation object
    gas_station = {
        "gasStationName": str(gasStationName),
        "gasStationAddress": str(gasStationAddress),
        "gasStationPhone": str(gasStationPhone),
        "location": geo_point,  # Use GeoPoint field for location
        "gasStationQueueLength": 0
    }

    # Add the data to Firestore
    try:
        doc_ref, doc_id = db.collection('gas_stations').add(gas_station)  # Access the document ID from the tuple
        print(f"Successfully added data for {gasStationName} with document ID: {doc_id}")
    except Exception as e:
        print(f"Failed to add data for {gasStationName}: {str(e)}")

# Close the Excel file when done
excel_file.close()
