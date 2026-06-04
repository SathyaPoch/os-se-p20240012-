import threading
import sys


def process_1(start_h, after_e):
    start_h.acquire()        
    sys.stdout.write("H")
    sys.stdout.write("E")
    after_e.release()        

def process_2(after_e, after_l1, after_l2):
    after_e.acquire()       
    sys.stdout.write("L")
    after_l1.release()       
    after_l1.acquire()       
    sys.stdout.write("L")
    after_l2.release()       

def process_3(after_l2):
    after_l2.acquire()       
    sys.stdout.write("O")


for i in range(1, 6):
    sys.stdout.write(f"Run {i}: ")
    sys.stdout.flush()
    
  
    start_h = threading.Semaphore(1)
    after_e = threading.Semaphore(0)
    after_l1 = threading.Semaphore(0)
    after_l2 = threading.Semaphore(0)
    
   
    threads = [
        threading.Thread(target=process_1, args=(start_h, after_e)),
        threading.Thread(target=process_2, args=(after_e, after_l1, after_l2)),
        threading.Thread(target=process_3, args=(after_l2,))
    ]
    
   
    for t in threads:
        t.start()
   
    for t in threads:
        t.join()
        
  
    sys.stdout.write("\n")
