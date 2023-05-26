import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Functionary (nome: String, cargo: String, salario: Float) {

}

fun main() {
    val file = File("funcionarios.csv")

    while (true) {

        println("Insira o número da funcionalidade que deseja utilizar: ")
        print("1 - Cadastrar funcionário\n2 - Listar os funcionários\n3 - Deletar funcionário\n4 - Encerrar o programa\n\nResposta: ")

        when (readln()) {
            "1" -> {
                registerFunctionary(file)
            }
            "2" -> {
                listFunctionary(file)
            }
            "3" -> {
                deleteFunctionary(file)
            }
            "4" -> {
                println("Programa encerrado")
                break
            }
            else -> {
                println("Por favor insira um valor válido.\n\n")
                continue
            }
        }
    }
}

fun registerFunctionary (file: File) {
    val reader = FileReader(file)
    val lines = reader.readLines().toMutableList()
    reader.close()

    print("Insira o nome do funcionário: ")
    var nome = readln()
    print("Insira o cargo do funcionário: ")
    var cargo = readln()
    print("Insira o salário do funcionário: ")
    var salario = readln().toFloat()

    if (nome.isEmpty()) {
        nome = "Vazio"
    }
    if (cargo.isEmpty()) {
        cargo = "Vazio"
    }
    if (salario.toString().isEmpty()) {
        salario = 0.0F
    }

    val novaLinha = "$nome,$cargo,$salario"
    lines.add(novaLinha)

    val writer = FileWriter(file)
    writer.write(lines.joinToString("\n"))
    writer.close()

    print("Funcionário cadastrado com sucesso!\n\n")
}

fun listFunctionary(file: File) {
    val reader = FileReader(file)
    val lines = reader.readLines()
    reader.close()

    if (lines.isEmpty()) {
        println("Não há nenhum funcionário cadastrado para exibir.\n\n")
        return
    }

    for (line in lines) {
        val fields = line.split(",")
        val nome = fields[0]
        val cargo = fields[1]
        val salario = fields[2]

        spacerTop(40)
        spacerCenter(40, "Nome", nome)
        spacerCenter(40, "Cargo", cargo)
        spacerCenter(40, "Salário", salario)
        spacerBotton(40)

        print("\n")
    }
}

fun deleteFunctionary(file: File) {
    val reader = FileReader(file)

    val lines = reader.readLines().toMutableList()
    reader.close()

    print("Digite o nome do funcionário que deseja deletar: ")
    val nome = readln()

    var index = -1

    val searchFunctionaries = File("searchFunctionaries.csv")
    val readerSearchFunctionaries = FileReader(searchFunctionaries)
    val linesSearchFunctionaries = readerSearchFunctionaries.readLines().toMutableList()

    for ((i, line) in lines.withIndex()) {
        val fields = line.split(",")
        if (fields[0] == nome) {
            index = i

            val novaLinha = "${fields[0]},${fields[1]},${fields[2]}"
            linesSearchFunctionaries.add(novaLinha)

            val writerSearchFunctionaries = FileWriter(searchFunctionaries)
            writerSearchFunctionaries.write(linesSearchFunctionaries.joinToString("\n"))
            writerSearchFunctionaries.close()

            linesSearchFunctionaries.size
        }
    }

    if (linesSearchFunctionaries.size > 1) {
        print("Foram encontrados mais de um funcionário com o nome informado. Informe o número do funcionário a ser deletado")

        var indexAux = 1;
        for (lineSearchFunctionaries in linesSearchFunctionaries) {
            val fields = lineSearchFunctionaries.split(",")
            val nome = fields[0]
            val cargo = fields[1]
            val salario = fields[2]

            print("$indexAux - ")
            spacerTop(40)
            spacerCenter(40, "Nome", nome)
            spacerCenter(40, "Cargo", cargo)
            spacerCenter(40, "Salário", salario)
            spacerBotton(40)

            indexAux++
            print("\n")
        }
    }

    if (index == -1) {
        println("Nenhum funcionário com o nome informado foi encontrado")
        return
    }

//    lines.removeAt(index)
//
//    val writer = FileWriter(file)
//    writer.write(lines.joinToString("\n"))
//    writer.close()
//
//    println("Funcionário deletado com sucesso")
}

fun spacerTop (size: Int) {
    print("╔")
    for (index in 1 .. size) {
        print("═")
    }
    print("╗\n")
}

fun spacerBotton (size: Int) {
    print("╚")
    for (index in 1 .. size) {
        print("═")
    }
    print("╝\n")
}

fun spacerCenter (size: Int, dataName: String, data: Any) {
    val text = "║ $dataName: $data"
    val newSize = size - text.length

    print(text)
    for (i in 1 .. newSize) {
        print(" ")
    }
    print(" ║\n")
}

