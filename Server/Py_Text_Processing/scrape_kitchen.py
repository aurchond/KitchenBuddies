# import requests
# from bs4 import BeautifulSoup

# url = "https://www.cooksinfo.com/kitchenware"

# def scrape_page(url):
#     # Make a GET request to the website
#     response = requests.get(url)

#     # Check if the request was successful
#     if response.status_code == 200:
#         # Parse the HTML content of the page
#         soup = BeautifulSoup(response.text, "html.parser")

#         # Find all the items in the "Kitchenware" section of the page
#         items = soup.find_all("h3", class_="page-list-ext-title")
#         # print(items)

#         # Loop over each item
#         for item in items:
#             # Extract the name and link of the item
#             name = item.find("a").text
#             link = item.find("a")["href"]

#             # Print the name and link of the item
#             print(f"{name}")
#             # print(f"Link: {link}")
#             scrape_page(link)

#             # If the item is a category, recursively scrape the items in that category
#             # if link.startswith("/category"):
                

#     # If the request was not successful, print an error message
#     else:
#         print(f"Failed to fetch the page. Response code: {response.status_code}")

# # Start the scrape at the kitchenware page
# scrape_page(url)

# import requests
# from bs4 import BeautifulSoup

# url = "https://en.wikipedia.org/wiki/List_of_food_preparation_utensils"

# def scrape_page(url):
#     # Make a GET request to the website
#     response = requests.get(url)

#     # Check if the request was successful
#     if response.status_code == 200:
#         # Parse the HTML content of the page
#         soup = BeautifulSoup(response.text, "html.parser")

#         # Find the table containing the list of food preparation utensils
#         table = soup.find("table", class_="wikitable plainrowheaders")

#         # Check if the table was found
#         if table:
#             # Find all the rows in the table
#             rows = table.find_all("tr")

#             # Loop over each row
#             for row in rows:
#                 # Find the first cell in the row
#                 cell = row.find("td")

#                 # If the cell exists, it contains the name of a food preparation utensil
#                 if cell:
#                     # Extract the text of the cell
#                     name = cell.text

#                     # Print the name of the food preparation utensil
#                     print(f"Name: {name}")
#         else:
#             print("Table not found.")
#     # If the request was not successful, print an error message
#     else:
#         print(f"Failed to fetch the page. Response code: {response.status_code}")

# # Start the scrape at the Wikipedia page
# scrape_page(url)

import requests
from bs4 import BeautifulSoup

def scrape_foods(url):
    response = requests.get(url)
    soup = BeautifulSoup(response.text, "html.parser")
    common_names = []
    for link in soup.find_all("a"):
        if "List of" in link.text:
            new_url = "https://en.wikipedia.org" + link.get("href")
            common_names.append(new_url)
            print(new_url)
            new_response = requests.get(new_url)
            new_soup = BeautifulSoup(new_response.text, "html.parser")
            table = new_soup.find("table")
            if table:
                for row in table.find_all("tr"):
                    cells = row.find_all("td")
                    if len(cells) > 0:
                        common_name = cells[0].text
                        print(common_name)
                        common_names.append(common_name)
    return common_names

# url = "https://en.wikipedia.org/wiki/Lists_of_foods"
# common_names = scrape_foods(url)
# print(common_names)

# with open("foods.txt", "w") as file:
#     for name in common_names:
#         try:
#             file.write(name + "\n")
#         except:
#             print('Error with ', name)

# print("Extracted common names written to foods.txt")

# def check_duplicates(file_path):
#     seen = set()
#     # duplicates = []
#     with open(file_path, "r") as file:
#         for line in file:
#             line = line.strip()
#             if line in seen:
#                 print(line)
#                 # duplicates.append(line)
#             else:
#                 seen.add(line)
#     return seen

# def write_to_file(values, file_path):
#     with open(file_path, "w") as file:
#         for value in values:
#             file.write(value + "\n")

# file_path = "foods.txt"
# unique_values = check_duplicates(file_path)
# write_to_file(unique_values, "unique_foods.txt")

url = "https://en.wikipedia.org/wiki/List_of_culinary_herbs_and_spices"
response = requests.get(url)
soup = BeautifulSoup(response.text, "html.parser")

spices = []
divs = soup.find_all("div", class_="div-col")
for div in divs:
    for li in div.find_all("li"):
        spice = li.find("a").text
        spices.append(spice)

print(spices)

def write_to_file(spices, file_path):
    with open(file_path, "a") as file:
        for spice in spices:
            file.write(spice + "\n")
        
file_path = "foods.txt"
write_to_file(spices, file_path)