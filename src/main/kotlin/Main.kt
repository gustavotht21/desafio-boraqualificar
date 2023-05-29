import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Functionary (val nome: String, val cargo: String, val salario: Float) {
    fun getData(): Map<String, Any> {
        return mapOf<String, Any>(
            "nome" to this.nome,
            "cargo" to this.cargo,
            "salario" to this.salario
        )
    }
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
                println(red() + "Por favor insira um valor válido.\n\n" + reset())
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

    val novoFuncionario = Functionary(nome, cargo, salario)
    val dataNovoFuncionario = novoFuncionario.getData()

    val novaLinha = "${dataNovoFuncionario["nome"]},${dataNovoFuncionario["cargo"]},${dataNovoFuncionario["salario"]}"
    lines.add(novaLinha)

    val writer = FileWriter(file)
    writer.write(lines.joinToString("\n"))
    writer.close()

    print(green() + "Funcionário cadastrado com sucesso!\n\n" + reset())
}

fun listFunctionary(file: File) {
    val reader = FileReader(file)
    val lines = reader.readLines()
    reader.close()

    if (lines.isEmpty()) {
        println(red() + "Não há nenhum funcionário cadastrado para exibir.\n" + reset())
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

    val index = mutableListOf(-1)

    val searchFunctionaries = File("searchFunctionaries.csv")
    val readerSearchFunctionaries = FileReader(searchFunctionaries)
    val linesSearchFunctionaries = readerSearchFunctionaries.readLines().toMutableList()

    for ((i, line) in lines.withIndex()) {
        val fields = line.split(",")
        if (fields[0] == nome) {
            if (index[0] == -1) {
                index.removeAt(0)
            }
            index.add(i)

            val novaLinha = "${fields[0]},${fields[1]},${fields[2]}"
            linesSearchFunctionaries.add(novaLinha)

            val writerSearchFunctionaries = FileWriter(searchFunctionaries)
            writerSearchFunctionaries.write(linesSearchFunctionaries.joinToString("\n"))
        }
    }

    if (index[0] == -1) {
        println(red() + "Nenhum funcionário com o nome informado foi encontrado" + reset())
        return
    }

    if (linesSearchFunctionaries.size > 1) {
        println("Foram encontrados mais de um funcionário com o nome informado. Informe o número do funcionário a ser deletado")

        var indexAux = 1;
        for (lineSearchFunctionaries in linesSearchFunctionaries) {
            val fields = lineSearchFunctionaries.split(",")
            val nome = fields[0]
            val cargo = fields[1]
            val salario = fields[2]

            println("$indexAux - ")
            spacerTop(40)
            spacerCenter(40, "Nome", nome)
            spacerCenter(40, "Cargo", cargo)
            spacerCenter(40, "Salário", salario)
            spacerBotton(40)

            indexAux++
            print("\n")
        }

        while (true) {
            print("Funcionário a ser deletado: (Insira \"0\" para cancelar a operação): ")
            val respostaDeleta = readln()

            when (respostaDeleta) {
                "0" -> {
                    println(red() + "Operação cancelada" + reset())
                    return
                }
                "" -> {
                    println(red() + "Por favor insira um valor válido.\n" + reset())
                    continue
                }
                else -> {
                    if (respostaDeleta.toInt() in lines.indices) {
                        lines.removeAt(index[respostaDeleta.toInt()-1])

                        val writer = FileWriter(file)
                        writer.write(lines.joinToString("\n"))
                        writer.close()
                        val writerSearchFunctionaries = FileWriter(searchFunctionaries)
                        writerSearchFunctionaries.write("")

                        println(green() + "Funcionário deletado com sucesso\n" + reset())
                        return
                    } else {
                        println(red() + "Por favor insira um valor válido.\n" + reset())
                        continue
                    }
                }
            }
        }
    }

    lines.removeAt(index[0])

    val writer = FileWriter(file)
    writer.write(lines.joinToString("\n"))
    writer.close()

    println(green() + "Funcionário deletado com sucesso\n\n" + reset())
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
