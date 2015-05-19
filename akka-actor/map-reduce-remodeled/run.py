import subprocess
import re
import time
from xlwt import Workbook
from collections import defaultdict
from datetime import datetime

def analyze(output):
    times = defaultdict(float)
    frequency = defaultdict(int)
    for line in output:
        k = re.search(r"(?<=\[)(.+?\))", line)
        if k is not None:
            k = k.group(1)
            v = re.search(r"(\d+)\.?(\d+)? secs", line)
            times[k] += float('.'.join(v.groups('0')))
            frequency[k] += 1
    return (times, frequency)

if __name__ == "__main__":
    command = "time java -jar -verbose:gc map_reduce_pi.jar (n_map) (n_reduce)"

    n_listLength = 5000
    n_repeated_times = 5
    list_length = [10, 100, 1000, 5000, 10000, 50000]

    wb = Workbook()
    name_col = {}
    col_n = 3
    sheet = wb.add_sheet("data")
    counter = 0
    for n_map in xrange(1, 9):
        print("map", n_map, datetime.now())
        for n_reduce in xrange(1, 9):
            print("reduce", n_reduce, datetime.now())
            for i_list_length in xrange(1):
                total = .0
                for i in xrange(0, n_repeated_times):
                    start = time.time()
                    output = subprocess.check_output(
                        command.replace("(n_map)", str(n_map)).replace(
                            "(n_reduce)", str(n_reduce)).split()).split("\n")
                    end = time.time()
                    total += end - start
                (times, frequency) = analyze(output)
                sheet.write(counter, 0, n_map)
                sheet.write(counter, 1, n_reduce)
                sheet.write(counter, 2, total / n_repeated_times)
                for k in times:
                    if k not in name_col:
                        name_col[k] = col_n
                        col_n += 2
                    sheet.write(counter, name_col[k],
                                times[k] / n_repeated_times)
                    sheet.write(counter, name_col[k] + 1,
                                float(frequency[k]) / n_repeated_times)
                counter += 1
		wb.save("test_result_pi.xls")
    m = wb.add_sheet("column names")
    for k in name_col:
        m.write(0, name_col[k], k)
    wb.save("test_result_pi.xls")
