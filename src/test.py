import pymysql

host = 'instacart.cwaxsjyuh73g.us-east-1.rds.amazonaws.com'
host = 'database-instacart.cwaxsjyuh73g.us-east-1.rds.amazonaws.com'
user = 'admin'
password = 'database1'
database = 'abc'
#database = 'database-instacart'

connection = pymysql.connect(host=host,
                             user=user,
                             password=password,
                             database=database)
with connection:
    cur = connection.cursor()
    cur.execute("select * from Employees")
    record = cur.fetchall()
    for row in record:
        print(row[0])
        print(row[1])
