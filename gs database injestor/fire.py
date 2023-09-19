import openpyxl
import requests

# Load the Excel file
excel_file = openpyxl.load_workbook('gas.xlsx')
sheet = excel_file.active

# Iterate through the rows of the sheet
for row in sheet.iter_rows(min_row=2, values_only=True):  # Assuming headers are in the first row
    gasStationName, gasStationAddress, gasStationPhone, gasStationLatitude, gasStationLongitude = row[1], row[4], row[9], row[15], row[16]

    # Create a dictionary to represent the GasStation object
    gas_station = {
        "gasStationName": str(gasStationName),
        "gasStationAddress": str(gasStationAddress),
        "gasStationPhone": str(gasStationPhone),
        "gasStationLatitude": float(gasStationLatitude),
        "gasStationLongitude": float(gasStationLongitude),
        "gasStationQueueLength": 0
    }

    # Send a POST request to add the data to the Firebase Realtime Database
    response = requests.post('https://gas-station-tracker-f16a4-default-rtdb.firebaseio.com/gas_stations.json', json=gas_station)

    # Check if the request was successful
    if response.status_code == 200:
        print(f"Successfully added data for {gasStationName}")
    else:
        print(f"Failed to add data for {gasStationName}")

# Close the Excel file when done
excel_file.close()
