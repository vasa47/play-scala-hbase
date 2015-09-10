
#!/bin/bash

# change the carId according to database rowid in your inserted database#

curl -X POST -H 'Content-Type: application/json' \
    -d '{"carId":"ff3ed15b-a7d6-430c-9dda-86fac789a201","name":"audi 76","fuel":"disel","price":"11","condition":"used","mileage":"11","firstreg":"yes"}' \
    localhost:9000/modifyCar


curl -X POST -H 'Content-Type: application/json' \
    -d '{"carId":"beea0965-a829-474f-8e7c-034d8ffbefe2","name":"audi 76","fuel":"disel","price":"11","condition":"used","mileage":"11","firstreg":"yes"}' \
    localhost:9000/getOneCar



curl -X POST -H 'Content-Type: application/json' \
    -d '{"carId":"beea0965-a829-474f-8e7c-034d8ffbefe2","name":"audi 76","fuel":"disel","price":"11","condition":"used","mileage":"11","firstreg":"yes"}' \
    localhost:9000/deleteCar





