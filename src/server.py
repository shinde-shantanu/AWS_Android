import tornado.ioloop
import tornado.web
import pymysql
import json
import time

class MainHandler(tornado.web.RequestHandler):
    def post(self):
        print("Request recieved")
        '''host = 'instacart.cwaxsjyuh73g.us-east-1.rds.amazonaws.com'
        user = 'admin'
        password = 'database1'
        database = 'instacart'

        connection = pymysql.connect(host=host,
                                     user=user,
                                     password=password,
                                     database=database)
        with connection:
            cur = connection.cursor()
            cur.execute("select * from aisles where aisle_id=1")
            record = cur.fetchall()
            print(type(record))
            for row in record:
                print(type(row))
                print(row[0])
                print(row[1])

        #print(self.request.remote_ip)
        print(self.request.body_arguments)'''
        ans={}
        try:
            start_time = time.time()
            host = 'instacart.cwaxsjyuh73g.us-east-1.rds.amazonaws.com'
            host = 'database-instacart.cwaxsjyuh73g.us-east-1.rds.amazonaws.com'
            user = 'admin'
            password = 'database1'
            data = json.loads(self.request.body)
            database = data["database"]

            connection = pymysql.connect(host=host,
                                         user=user,
                                         password=password,
                                         database=database)
            
            ans['isError']="False"
            with connection:
                cur = connection.cursor()
                #"select * from aisles where aisle_id=1"
                cur.execute(data["query"])
                record = cur.fetchall()
                ans['count']=str(len(record))
                ans['output']=[]
                columns=[str(x) for x in record[0]]
                ans['columns']=str(columns)[1:len(str(columns))-1]
                print(type(record))
                for row in record:
                    #row=tuple(map(str,row))
                    #ans['output'].append({str(i):row})
                    #i+=1
                    print(row)
                    r={}
                    for i in range(len(row)):
                        #r.append(x)
                        r[str(i)]=row[i]
                    ans['output'].append(r)
                    #ans['output']+=str(row).split('(')[1].split(')')[0].split("\\r")[0]+'\''+"\n"
                    #print(row[0])
                    #print(row[1])
                start_time = time.time()
                print(time.time(),start_time)
                ans['time']=time.time() - start_time
                print(list(columns))
                print(ans)
        except Exception as e:
            ans['isError']="True"
            ans['output']=str(e)
            ans['time']=time.time() - start_time
            print(e)
            pass
        #print(self.get_body_argument('query'))
        self.write(ans)

def make_app():
    return tornado.web.Application([
        (r"/", MainHandler),
    ])

if __name__ == "__main__":
    app = make_app()
    app.listen(8888)
    tornado.ioloop.IOLoop.current().start()
