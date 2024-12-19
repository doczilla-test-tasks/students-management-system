function getStudentsBatchUrl(limit, offset) {
    baseUrl = "http://localhost:4567/api/v1/students";
    return baseUrl + "?limit=" + limit + "&offset=" + offset;
}

function deleteStudentByIdUrl(id) {
    baseUrl = "http://localhost:4567/api/v1/students";
    return baseUrl + "/delete/" + id;
}

function getStudentsBatch(limit, offset) {
    $.get({
        url: getStudentsBatchUrl(limit, offset * limit),
        dataType: "json",
        success: function(students) {
            $("#students-table tbody").html("");
            $.each(students, function (idx, student) { 
                $("#students-table tbody").append(
                    $("<tr></tr>")
                        .data("student", student)
                        .append(
                            $("<td></td>").append(
                                offset * limit + idx + 1
                            ),
                            $("<td></td>").append(student["surname"]),
                            $("<td></td>").append(student["name"]),
                            $("<td></td>").append(student["patronymic"] ?? "-"),
                            $("<td></td>").append(new Date(student["birthDate"]).toLocaleDateString("ru-RU")),
                            $("<td></td>").append(student["group"]),
                            $("<input></input>", { "class": "delete-button", "name": "delete", "type": "button"}).val("Delete")
                        )
                );
            });

            $("input.delete-button:button").click(function(e) {
                var deletingStudentId = $(this).parent().data("student").id;
                $.ajax({
                    url: deleteStudentByIdUrl(deletingStudentId),
                    dataType: "json",
                    method: "DELETE",
                    global: true,
                    success: function(deletionStatus) {
                        console.log("Student with id " + deletingStudentId + " deletion status: " + deletionStatus);
                    },
                    error: function(err) {
                        console.log(err);
                    }
                });
            });
            
            start = offset * limit + 1;
            end = (offset + 1) * limit;
            $(".table-description").html(
                $("<p></p>").append(start + " to " + end)
            );
        },
        error: function(err) {
            console.log(err);
        }
    });
}

$(document).ready(function () {
    $("#students-table").data("offset", 0);
    getStudentsBatch(parseInt($("input:radio[name='limit']:checked").val()), $("#students-table").data("offset"));
   
    $(document).on("ajaxComplete", function(e, xhr, opts) {
        var getStudentsBatchUrlRegex = new RegExp(".*\/api\/v1\/students\\?limit=\\d+&offset=\\d+");
        if ((!getStudentsBatchUrlRegex.test(opts.url))) {
            console.log("Resend request for table data");
            getStudentsBatch(parseInt($("input:radio[name='limit']:checked").val()), $("#students-table").data("offset"));
        }
    });

    $("input:radio[name='limit']").on("change", function() {
        getStudentsBatch(parseInt($(this).val()), $("#students-table").data("offset"));
    });

    //TODO: make some restrictions for "next" button
    $("input:button[name='next']").click(function(e) {
        var cur_offset = $("#students-table").data("offset");
        $("#students-table").data("offset", cur_offset + 1);
        getStudentsBatch(parseInt($("input:radio[name='limit']:checked").val()), $("#students-table").data("offset"));
    });
    $("input:button[name='prev']").click(function(e) {
        var cur_offset = $("#students-table").data("offset");
        if (cur_offset > 0) {
            $("#students-table").data("offset", cur_offset - 1);
        } else {
            console.log("Unable to find previous page");
        }
        getStudentsBatch(parseInt($("input:radio[name='limit']:checked").val()), $("#students-table").data("offset"));
    });
});