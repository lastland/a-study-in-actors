import subprocess
import re
import time
from xlwt import Workbook
from collections import defaultdict


def analyze(output):
    times = defaultdict(float)
    for line in output:
        k = re.search(r"(?<=\[)(.+?\))", line)
        if k is not None:
            k = k.group(1)
            v = re.search(r"(\d+)\.?(\d+)? secs", line)
            times[k] += float('.'.join(v.groups('0')))
    return times

if __name__ == "__main__":
    command = "time java -jar -verbose:gc map_reduce.jar (n_actors) (n_listLength)"

    n_listLength = 5000
    n_repeated_times = 5

    wb = Workbook()
    name_col = {}
    col_n = 1
    sheet = wb.add_sheet("data")
    for n_actors in xrange(1, 33):
        total = .0
        for i in xrange(0, n_repeated_times):
            start = time.time()
            output = subprocess.check_output(
                command.replace("(n_actors)", str(n_actors)).replace(
                    "(n_listLength)", str(n_listLength)).split()).split("\n")
            end = time.time()
            total += end - start
            times = analyze(output)
        sheet.write(n_actors - 1, 0, total / n_repeated_times)
        for k in times:
            if k not in name_col:
                name_col[k] = col_n
                col_n += 1
            sheet.write(n_actors - 1, name_col[k], times[k] / n_repeated_times)
    m = wb.add_sheet("column names")
    for k in name_col:
        m.write(0, name_col[k], k)
    wb.save("test_with_different_number_of_actors.xls")

    n_actors = 4

    wb = Workbook()
    name_col = {}
    col_n = 1
    sheet = wb.add_sheet("data")
    for n_listLength in [10, 100, 1000, 10000]:
        total = .0
        for i in xrange(0, n_repeated_times):
            start = time.time()
            output = subprocess.check_output(
                command.replace("(n_actors)", str(n_actors)).replace(
                    "(n_listLength)", str(n_listLength)).split()).split("\n")
            end = time.time()
            total += end - start
            times = analyze(output)
        sheet.write(n_listLength - 1, 0, total / n_repeated_times)
        for k in times:
            if k not in name_col:
                name_col[k] = col_n
                col_n += 1
            sheet.write(n_listLength - 1, name_col[k], times[k] / n_repeated_times)
    m = wb.add_sheet("column names")
    for k in name_col:
        m.write(0, name_col[k], k)
    wb.save("test_with_different_lengths_of_list.xls")
