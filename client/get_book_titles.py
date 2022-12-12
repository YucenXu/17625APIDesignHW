from inventory_client import InventoryClient

def getBookTitles(inventoryClient):
    ISBNList = ["0", "0137133367"]
    titleList = []
    for ISBN in ISBNList:
        book = inventoryClient.get_book(ISBN)
        titleList.append(book.title)
    return titleList

if __name__ == '__main__':
    # Init client
    inventoryClient = InventoryClient()
    # Get title list 
    titleList = getBookTitles(inventoryClient)
    print(titleList)