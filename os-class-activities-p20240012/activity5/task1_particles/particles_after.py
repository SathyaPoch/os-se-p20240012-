import threading
import time
import sys
import os

empty_pairs = threading.Semaphore(50)
full_pairs = threading.Semaphore(0)
mutex = threading.Lock() 
buffer = []
produced_count = 0
packaged_count = 0

def producer(machine_id):
    global produced_count
    pair_id = 0
    while True:
        pair_id += 1
        p1 = f"M{machine_id}-{pair_id}-P1"
        p2 = f"M{machine_id}-{pair_id}-P2"
        
        empty_pairs.acquire()
        mutex.acquire()
        
     
        if len(buffer) >= 100:
            print("\nThe producing machine is broken")
            os._exit(1)
            
        buffer.append(p1)
        buffer.append(p2)
        produced_count += 1
        
        mutex.release()
        full_pairs.release()
        time.sleep(0.01) # Slight delay for readability

def consumer():
    global packaged_count
    while True:
        full_pairs.acquire()
        mutex.acquire()
        
        if len(buffer) < 2:
            print("\nThe packaging machine is broken")
            os._exit(1)
            
        p1 = buffer.pop(0)
        p2 = buffer.pop(0)
        
        base1 = p1.rsplit('-', 1)[0]
        base2 = p2.rsplit('-', 1)[0]
        if base1 != base2:
            print("\nPairs are incorrect")
            os._exit(1)
            
        packaged_count += 1
        print(f"Produced pairs: {produced_count} | Packaged pairs: {packaged_count} | Buffer particles: {len(buffer)}")
        
        mutex.release()
        empty_pairs.release()
        time.sleep(0.01)


for i in range(1, 4):
    threading.Thread(target=producer, args=(i,), daemon=True).start()

threading.Thread(target=consumer, daemon=True).start()

print("Running safe simulation. Press Ctrl+C to stop.")
try:
    while True:
        time.sleep(1)
except KeyboardInterrupt:
    print("\nSimulation stopped safely.")