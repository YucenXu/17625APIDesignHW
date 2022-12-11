# Copyright 2015 gRPC authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""Common resources used in the gRPC route guide example."""

import json

import inventory_pb2

# Read from database and return book list
def read_book_database():
    book_list = []
    with open("book_db.json") as book_db_file:
        for item in json.load(book_db_file):
            book = inventory_pb2.Book(
                ISBN=item['ISBN'],
                title=item['title'],
                author=item['author'],
                genre=item['genre'],
                year=item['year']
            )
            book_list.append(book)

    return book_list

# Write a new book to database
def write_book_database(book):
    with open("book_db.json", "r+") as book_db_file:
        book_db_data = json.load(book_db_file)
        book_db_data.append(book)
        book_db_file.seek(0)
        json.dump(book_db_data, book_db_file, indent=4)
