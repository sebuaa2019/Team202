#!/usr/bin/env python

import rospy
import socket
from geometry_msgs.msg import Twist

global conn
global soc

def sendtoclient(msg):
    global conn, soc
    x = ('%.3f' % abs(msg.linear.x))
    z = ('%.3f' % abs(msg.angular.z))
    if ((x > 0 or z > 0)):
        try:
            conn.send("("+str(x)+","+str(z)+")")
        except Exception:
            conn, addr = soc.accept()
            

if __name__ == "__main__":
    global conn, soc
    #HOST = rospy.get_param('HOST')
    #Info_Port = rospy.get_param('Info_Port')
    soc = socket.socket()
    #soc.bind((HOST, Info_Port))
    soc.bind(('192.168.43.229', 2000))
    rospy.init_node('info_server')
    print('Info-Server Online!')
    soc.listen(5)
    conn, addr = soc.accept()
    print("Info-Server >> Connection From : " + addr[0])
    rospy.Subscriber('/cmd_vel', Twist, sendtoclient)
    rospy.spin()
