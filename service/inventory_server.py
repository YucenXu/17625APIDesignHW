from concurrent import futures
import logging

import grpc
import inventory_pb2
import inventory_pb2_grpc
import inventory_resources

class InventoryServiceServicer(inventory_pb2_grpc.InventoryServiceServicer):
    
    # Create a New Book
    def CreateBook(self, request, context):
        # Get request body
        ISBN = request.ISBN
        title = request.title
        author = request.author
        genre = list(request.genre)
        year = request.year

        # Read database
        db = inventory_resources.read_book_database()

        # Look for book
        for book in db:
            # Found same book, can't create new one
            if book.ISBN == ISBN:
                return inventory_pb2.CreateBookResponse(msg="Book Already Exists")
        
        # Create new book
        newBook = {
            "ISBN": ISBN,
            "title": title,
            "author": author,
            "genre": genre,
            "year": year
        }

        # Add new book to database
        inventory_resources.write_book_database(newBook)
        return inventory_pb2.CreateBookResponse(msg="Successfuly Created Book")

    # Get book by ISBN
    def GetBook(self, request, context):
        # Get ISBN
        ISBN = request.ISBN

        # Read database
        db = inventory_resources.read_book_database()
        for book in db:
            # Found
            if book.ISBN == ISBN:
                return inventory_pb2.Book(ISBN=book.ISBN, title=book.title, author=book.author, genre=book.genre, year=book.year)
        
        # Book not found
        return inventory_pb2.Book(ISBN="", title="", author="", genre=[], year=0)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    inventory_pb2_grpc.add_InventoryServiceServicer_to_server(
        InventoryServiceServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()


if __name__ == '__main__':
    logging.basicConfig()
    serve()
