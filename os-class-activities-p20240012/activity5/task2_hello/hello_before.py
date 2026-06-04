import threading
import time
import sys
import random

def process_1():
    time.sleep(random.uniform(0.001, 0.01))
    sys.stdout.write("H")
    time.sleep(random.uniform(0.001, 0.01))
    sys.stdout.write("E")

def process_2():
    time.sleep(random.uniform(0.001, 0.01))
    sys.stdout.write("L")
    time.sleep(random.uniform(0.001, 0.01))
    sys.stdout.write("L")

def process_3():
    time.sleep(random.uniform(0.001, 0.01))
    sys.stdout.write("O")


for i in range(1, 6):
    sys.stdout.write(f"Run {i}: ")
    sys.stdout.flush()
    

    threads = [
        threading.Thread(target=process_1),
        threading.Thread(target=process_2),
        threading.Thread(target=process_3)
    ]
  
    for t in threads:
        t.start()
        
  
    for t in threads:
        t.join()
 
    sys.stdout.write("\n")

