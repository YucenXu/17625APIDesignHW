from unittest import TestCase
from unittest.mock import patch
from inventory_client import InventoryClient
import inventory_pb2
import get_book_titles

class TestInventory(TestCase):
    def mock_get_book(self,ISBN):
        mock_database = [
            inventory_pb2.Book(ISBN="0", title="Test Book", author="Anonymous Author", genre=["test"], year=2022),
            inventory_pb2.Book(ISBN="0137133367", title="CSAPP", author="Randal Bryant", genre=[ "computer", "OS"], year=2010),
        ]
        for book in mock_database:
            if book.ISBN == ISBN:
                return book
    
    @patch('inventory_client.InventoryClient')
    def test_inventory_client_mock(self, InventoryClient):
        print("----------Start Testing Mock Server----------")
        client = InventoryClient()
        client.get_book = self.mock_get_book
        response = get_book_titles.getBookTitles(client)
        print("response:" + str(response))
        self.assertEqual(response, ["Test Book", "CSAPP"])
        print("----------Test End----------\n\n")
    
    
    def test_inventory_client(self):
        print("----------Start Testing Actural Server----------")
        client = InventoryClient()
        response = get_book_titles.getBookTitles(client)
        print("response:" + str(response))
        self.assertEqual(response, ["Test Book", "CSAPP"])
        print("----------Test End----------\n\n")