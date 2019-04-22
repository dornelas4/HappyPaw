from flask import Flask
from flask_restplus import Api, Resource, fields


flask_app = Flask(__name__)
app = Api(app=flask_app)

container_percentage = 55

name_space = app.namespace('main', description='Main APIs')
schedule = app.namespace('schedule', description='Main APIs')
food_container = app.namespace('container', description='Main APIs')
list1 = []

model = app.model('Name Model', 
		  {
           'mealPerDay':fields.Integer(required = True, description="Meal per day for dog", help="Must be more than 0"),
        #    'firstMealHour':fields.Integer(),
        #    'firstMealMinute':fields.Integer(),
        #    'intervalHour':fields.Integer(),
        #    'intervalMinute':fields.Integer()
           })

@name_space.route("/")
class MainClass(Resource):
    def get(self):
        return {
            "status": "Got new data"
        }

    def post(self):
        return {
            "status": "Posted new data"
        }


@schedule.route("/")
class Schedule(Resource):
    def get(self):
        return {
            "status": "Exit"
        }

    @app.expect(model)
    def post(self):
        return {
            "status": model["mealPerDay"]
        }

@food_container.route("/")
class FoodContainer(Resource):
    def get(self):
        return {
            "ID": 1,
            "capacity": 2,
            "fillPercentage": container_percentage
        }

# if __name__ == '__main__':
#     app.run(debug=True)
