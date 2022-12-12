from __future__ import print_function

import grpc
import inventory_pb2
import inventory_pb2_grpc

class InventoryClient():
    def get_book(self,ISBN):
        book = self.stub.GetBook(inventory_pb2.GetBookRequest(ISBN=ISBN))
        return book

    def create_book(self, book):
        ISBN = book['ISBN']
        title = book['title']
        author = book['author']
        genre = list(book['genre'])
        year = int(book['year'])

        msg = self.stub.CreateBook(inventory_pb2.Book(ISBN=ISBN, title=title, author=author, genre=genre, year=year))
        return msg

    def __init__(self):
        self.stub = inventory_pb2_grpc.InventoryServiceStub(grpc.insecure_channel('localhost:50051'))
