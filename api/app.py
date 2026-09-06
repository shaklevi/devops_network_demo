from flask import Flask, jsonify
import psycopg2
import os

app = Flask(__name__)

DB_HOST = os.getenv("DB_HOST", "db")
DB_NAME = os.getenv("DB_NAME", "demo")
DB_USER = os.getenv("DB_USER", "demo")
DB_PASSWORD = os.getenv("DB_PASSWORD", "secret")


@app.route("/")
def index():
    try:
        connection = psycopg2.connect(
            host=DB_HOST,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )

        cursor = connection.cursor()

        cursor.execute("SELECT message FROM messages LIMIT 1;")

        row = cursor.fetchone()

        cursor.close()
        connection.close()

        return jsonify({
            "api": "API is working",
            "database_message": row[0]
        })

    except Exception as error:
        return jsonify({
            "api": "API is working",
            "database_error": str(error)
        }), 500


@app.route("/health")
def health():
    return jsonify({
        "status": "healthy"
    })


app.run(
    host="0.0.0.0",
    port=5000
)
